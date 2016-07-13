package com.dreamliner.simplifyokhttp.builder;

import com.dreamliner.simplifyokhttp.request.PostStringRequest;
import com.dreamliner.simplifyokhttp.request.RequestCall;

import okhttp3.MediaType;

/**
 * @author chenzj
 * @Title: PostStringBuilder
 * @Description: 类的描述 - post字符串建造器
 * @date 2016/3/19 18:45
 * @email admin@chenzhongjin.cn
 */
public class PostStringBuilder extends OkHttpRequestBuilder<PostStringBuilder> {

    private String content;
    private MediaType mediaType;


    public PostStringBuilder content(String content) {
        this.content = content;
        return this;
    }

    public PostStringBuilder mediaType(MediaType mediaType) {
        this.mediaType = mediaType;
        return this;
    }

    @Override
    public RequestCall build() {
        return new PostStringRequest(url, tag, params, headers, content, mediaType, id).build();
    }
}
