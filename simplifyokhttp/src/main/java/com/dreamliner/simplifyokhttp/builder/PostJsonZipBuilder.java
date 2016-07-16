/*
 * Copyright (c) 2016  DreamLiner Studio
 * Licensed under the Apache License, Version 2.0 (the "License”);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
public class PostJsonZipBuilder extends OkHttpRequestBuilder<PostJsonZipBuilder> implements HasParamsable {

    private String mJsonStr = "";
    private MediaType mediaType;

    @Override
    public RequestCall build() {
        return new PostJsonZipRequest(url, tag, params, headers, mJsonStr, mediaType, id).build();
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
}
