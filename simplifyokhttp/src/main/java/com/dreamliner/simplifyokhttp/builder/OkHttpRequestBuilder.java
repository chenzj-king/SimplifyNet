package com.dreamliner.simplifyokhttp.builder;

import com.dreamliner.simplifyokhttp.request.RequestCall;

import java.util.Map;

/**
 * @author chenzj
 * @Title: OkHttpRequestBuilder
 * @Description: 类的描述 - 请求建造器抽象类
 * @date 2016/3/19 18:45
 * @email admin@chenzhongjin.cn
 */
public abstract class OkHttpRequestBuilder {

    protected String url;
    protected Object tag;
    protected Map<String, String> headers;
    protected Map<String, String> params;

    public abstract OkHttpRequestBuilder url(String url);

    public abstract OkHttpRequestBuilder tag(Object tag);

    public abstract OkHttpRequestBuilder headers(Map<String, String> headers);

    public abstract OkHttpRequestBuilder addHeader(String key, String val);

    public abstract RequestCall build();
}
