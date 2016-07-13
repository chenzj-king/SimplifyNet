package com.dreamliner.simplifyokhttp.builder;

import com.dreamliner.simplifyokhttp.request.OtherRequest;
import com.dreamliner.simplifyokhttp.request.RequestCall;

import okhttp3.RequestBody;

/**
 * @author chenzj
 * @Title: OtherRequestBuilder
 * @Description: 类的描述 - DELETE、PUT、PATCH等其他方法
 * @date 2016/3/19 18:45
 * @email admin@chenzhongjin.cn
 */
public class OtherRequestBuilder extends OkHttpRequestBuilder<OtherRequestBuilder> {

    private RequestBody requestBody;
    private String method;
    private String content;

    public OtherRequestBuilder(String method) {
        this.method = method;
    }

    @Override
    public RequestCall build() {
        return new OtherRequest(requestBody, content, method, url, tag, params, headers, id).build();
    }

    public OtherRequestBuilder requestBody(RequestBody requestBody) {
        this.requestBody = requestBody;
        return this;
    }

    public OtherRequestBuilder requestBody(String content) {
        this.content = content;
        return this;
    }
}
