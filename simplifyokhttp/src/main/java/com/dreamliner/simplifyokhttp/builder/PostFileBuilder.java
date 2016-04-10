package com.dreamliner.simplifyokhttp.builder;

import com.dreamliner.simplifyokhttp.request.PostFileRequest;
import com.dreamliner.simplifyokhttp.request.RequestCall;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import okhttp3.MediaType;

/**
 * @author chenzj
 * @Title: PostFileBuilder
 * @Description: 类的描述 - post文件建造器
 * @date 2016/3/19 18:45
 * @email admin@chenzhongjin.cn
 */
public class PostFileBuilder extends OkHttpRequestBuilder {

    private File file;
    private MediaType mediaType;

    public OkHttpRequestBuilder file(File file) {
        this.file = file;
        return this;
    }

    public OkHttpRequestBuilder mediaType(MediaType mediaType) {
        this.mediaType = mediaType;
        return this;
    }

    @Override
    public RequestCall build() {
        return new PostFileRequest(url, tag, params, headers, file, mediaType).build();
    }

    @Override
    public PostFileBuilder url(String url) {
        this.url = url;
        return this;
    }

    @Override
    public PostFileBuilder tag(Object tag) {
        this.tag = tag;
        return this;
    }

    @Override
    public PostFileBuilder headers(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    @Override
    public PostFileBuilder addHeader(String key, String val) {
        if (this.headers == null) {
            headers = new LinkedHashMap<>();
        }
        headers.put(key, val);
        return this;
    }
}
