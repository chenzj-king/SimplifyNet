package com.dreamliner.simplifyokhttp.request;

import java.util.Map;

import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * @author chenzj
 * @Title: GetRequest
 * @Description: 类的描述 - 生成okhttp get request
 * @date 2016/3/19 18:45
 * @email admin@chenzhongjin.cn
 */
public class GetRequest extends OkHttpRequest {

    public GetRequest(String url, Object tag, Map<String, String> params, Map<String, String> headers) {
        super(url, tag, params, headers);
    }

    @Override
    protected RequestBody buildRequestBody() {
        return null;
    }

    @Override
    protected Request buildRequest(RequestBody requestBody) {
        return builder.get().build();
    }


}
