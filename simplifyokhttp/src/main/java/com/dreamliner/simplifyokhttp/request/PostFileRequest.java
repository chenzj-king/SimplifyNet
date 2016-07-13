package com.dreamliner.simplifyokhttp.request;

import com.dreamliner.simplifyokhttp.OkHttpUtils;
import com.dreamliner.simplifyokhttp.callback.HttpCallBack;
import com.dreamliner.simplifyokhttp.utils.Exceptions;

import java.io.File;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * @author chenzj
 * @Title: PostFileRequest
 * @Description: 类的描述 - 生成okhttp get request
 * @date 2016/3/19 18:45
 * @email admin@chenzhongjin.cn
 */
public class PostFileRequest extends OkHttpRequest {

    private static MediaType MEDIA_TYPE_STREAM = MediaType.parse("application/octet-stream");

    private File file;
    private MediaType mediaType;

    public PostFileRequest(String url, Object tag, Map<String, String> params, Map<String, String> headers, File file, MediaType
            mediaType, int id) {
        super(url, tag, params, headers, id);
        this.file = file;
        this.mediaType = mediaType;

        if (this.file == null) {
            Exceptions.illegalArgument("the file can not be null !");
        }
        if (this.mediaType == null) {
            this.mediaType = MEDIA_TYPE_STREAM;
        }
    }

    @Override
    protected RequestBody buildRequestBody() {
        return RequestBody.create(mediaType, file);
    }

    @Override
    protected RequestBody wrapRequestBody(RequestBody requestBody, final HttpCallBack httpCallBack) {
        if (httpCallBack == null) return requestBody;
        CountingRequestBody countingRequestBody = new CountingRequestBody(requestBody, new CountingRequestBody.Listener() {
            @Override
            public void onRequestProgress(final long bytesWritten, final long contentLength) {

                OkHttpUtils.getInstance().getDelivery().execute(new Runnable() {
                    @Override
                    public void run() {
                        httpCallBack.inProgress(bytesWritten * 1.0f / contentLength);
                    }
                });

            }
        });
        return countingRequestBody;
    }

    @Override
    protected Request buildRequest(RequestBody requestBody) {
        return builder.post(requestBody).build();
    }
}
