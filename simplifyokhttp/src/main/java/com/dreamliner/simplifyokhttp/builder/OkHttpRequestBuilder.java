package com.dreamliner.simplifyokhttp.builder;

import com.dreamliner.simplifyokhttp.request.RequestCall;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author chenzj
 * @Title: OkHttpRequestBuilder
 * @Description: 类的描述 - 请求建造器抽象类
 * @date 2016/3/19 18:45
 * @email admin@chenzhongjin.cn
 */
public abstract class OkHttpRequestBuilder<T extends OkHttpRequestBuilder> {

    protected String url;
    protected Object tag;
    protected Map<String, String> headers;
    protected Map<String, String> params;
    protected int id;

    public T id(int id) {
        this.id = id;
        return (T) this;
    }

    public T url(String url) {
        this.url = url;
        return (T) this;
    }


    public T tag(Object tag) {
        this.tag = tag;
        return (T) this;
    }

    public T headers(Map<String, String> headers) {
        this.headers = headers;
        return (T) this;
    }

    public T addHeader(String key, String val) {
        if (this.headers == null) {
            headers = new LinkedHashMap<>();
        }
        headers.put(key, val);
        return (T) this;
    }

    public abstract RequestCall build();
}
