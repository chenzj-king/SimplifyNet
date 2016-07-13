package com.dreamliner.simplifyokhttp.builder;

import com.dreamliner.simplifyokhttp.request.PostFileRequest;
import com.dreamliner.simplifyokhttp.request.RequestCall;

import java.io.File;

import okhttp3.MediaType;

/**
 * @author chenzj
 * @Title: PostFileBuilder
 * @Description: 类的描述 - post文件建造器
 * @date 2016/3/19 18:45
 * @email admin@chenzhongjin.cn
 */
public class PostFileBuilder extends OkHttpRequestBuilder<PostFileBuilder> {

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
        return new PostFileRequest(url, tag, params, headers, file, mediaType, id).build();
    }
}
