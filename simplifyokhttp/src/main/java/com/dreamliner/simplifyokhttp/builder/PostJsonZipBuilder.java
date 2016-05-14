package com.dreamliner.simplifyokhttp.builder;


import com.dreamliner.simplifyokhttp.request.PostJsonZipRequest;
import com.dreamliner.simplifyokhttp.request.RequestCall;

import java.util.LinkedHashMap;
import java.util.Map;

import okhttp3.MediaType;

/**
 * @author chenzj
 * @Title: PostFormBuilder
 * @Description: 类的描述 - post参数的建造器
 * @date 2016/3/19 18:45
 * @email admin@chenzhongjin.cn
 */
public class PostJsonZipBuilder extends OkHttpRequestBuilder implements HasParamsable {

    private String mJsonStr = "";
    private MediaType mediaType;

    @Override
    public RequestCall build() {
        return new PostJsonZipRequest(url, tag, params, headers, mJsonStr, mediaType).build();
    }

    public PostJsonZipBuilder addMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
        return this;
    }

    public PostJsonZipBuilder addJsonStr(String jsonStr) {
        this.mJsonStr = jsonStr;
        return this;
    }

    @Override
    public PostJsonZipBuilder url(String url) {
        this.url = url;
        return this;
    }

    @Override
    public PostJsonZipBuilder tag(Object tag) {
        this.tag = tag;
        return this;
    }

    @Override
    public PostJsonZipBuilder params(Map<String, String> params) {
        this.params = params;
        return this;
    }

    @Override
    public PostJsonZipBuilder addParams(String key, String val) {
        if (this.params == null) {
            params = new LinkedHashMap<>();
        }
        params.put(key, val);
        return this;
    }

    @Override
    public PostJsonZipBuilder headers(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }


    @Override
    public PostJsonZipBuilder addHeader(String key, String val) {
        if (this.headers == null) {
            headers = new LinkedHashMap<>();
        }
        headers.put(key, val);
        return this;
    }
}
