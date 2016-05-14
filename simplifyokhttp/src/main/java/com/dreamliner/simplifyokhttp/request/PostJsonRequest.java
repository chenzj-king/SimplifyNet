package com.dreamliner.simplifyokhttp.request;

import android.text.TextUtils;

import com.dreamliner.simplifyokhttp.utils.Exceptions;

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
public class PostJsonRequest extends OkHttpRequest {

    private static MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

    private String mJsonStr;
    private MediaType mediaType;

    public PostJsonRequest(String url, Object tag, Map<String, String> params, Map<String, String> headers, String mJsonStr, MediaType
            mediaType) {
        super(url, tag, params, headers);
        this.mJsonStr = mJsonStr;
        this.mediaType = mediaType;

        if (TextUtils.isEmpty(mJsonStr)) {
            Exceptions.illegalArgument("the jsonStr can not be null !");
        }
        if (this.mediaType == null) {
            this.mediaType = MEDIA_TYPE_JSON;
        }
    }

    @Override
    protected RequestBody buildRequestBody() {
        return RequestBody.create(mediaType, mJsonStr);
    }

    @Override
    protected Request buildRequest(RequestBody requestBody) {
        return builder.post(requestBody).build();
    }
}
