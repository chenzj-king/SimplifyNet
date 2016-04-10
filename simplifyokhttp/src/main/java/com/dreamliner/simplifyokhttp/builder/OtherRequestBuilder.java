package com.dreamliner.simplifyokhttp.builder;

import com.dreamliner.simplifyokhttp.request.OtherRequest;
import com.dreamliner.simplifyokhttp.request.RequestCall;

import java.util.LinkedHashMap;
import java.util.Map;

import okhttp3.RequestBody;

/**
 * @author chenzj
 * @Title: OtherRequestBuilder
 * @Description: 类的描述 - DELETE、PUT、PATCH等其他方法
 * @date 2016/3/19 18:45
 * @email admin@chenzhongjin.cn
 */
public class OtherRequestBuilder extends OkHttpRequestBuilder {

    private RequestBody requestBody;
    private String method;
    private String content;

    public OtherRequestBuilder(String method) {
        this.method = method;
    }

    @Override
    public RequestCall build() {
        return new OtherRequest(requestBody, content, method, url, tag, params, headers).build();
    }

    public OtherRequestBuilder requestBody(RequestBody requestBody) {
        this.requestBody = requestBody;
        return this;
    }

    public OtherRequestBuilder requestBody(String content) {
        this.content = content;
        return this;
    }

    @Override
    public OtherRequestBuilder url(String url) {
        this.url = url;
        return this;
    }

    @Override
    public OtherRequestBuilder tag(Object tag) {
        this.tag = tag;
        return this;
    }


    @Override
    public OtherRequestBuilder headers(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    @Override
    public OtherRequestBuilder addHeader(String key, String val) {
        if (this.headers == null) {
            headers = new LinkedHashMap<>();
        }
        headers.put(key, val);
        return this;
    }
}
