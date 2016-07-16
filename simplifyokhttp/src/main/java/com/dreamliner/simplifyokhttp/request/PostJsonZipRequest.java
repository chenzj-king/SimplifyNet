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

package com.dreamliner.simplifyokhttp.request;

import android.text.TextUtils;

import com.dreamliner.simplifyokhttp.utils.Exceptions;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * @author chenzj
 * @Title: PostFileRequest
 * @Description: 类的描述 - 生成okhttp get request
 * @date 2016/3/19 18:45
 * @email admin@chenzhongjin.cn
 */
public class PostJsonZipRequest extends OkHttpRequest {

    private static MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

    private String mJsonStr;
    private MediaType mediaType;

    public PostJsonZipRequest(String url, Object tag, Map<String, String> params, Map<String, String> headers, String mJsonStr,
                              MediaType mediaType, int id) {
        super(url, tag, params, headers, id);
        this.mJsonStr = mJsonStr;
        this.mediaType = mediaType;

        if (TextUtils.isEmpty(mJsonStr)) {
            Exceptions.illegalArgument("the jsonStr can not be null !");
        }
        if (this.mediaType == null) {
            this.mediaType = MEDIA_TYPE_JSON;
        }
    }

    @Override
    protected RequestBody buildRequestBody() {

        byte[] data = new byte[0];
        try {
            data = mJsonStr.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream arr = new ByteArrayOutputStream();
        OutputStream zipper = null;
        try {
            zipper = new GZIPOutputStream(arr);
            zipper.write(data);
            zipper.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return RequestBody.create(mediaType, arr.toByteArray());
    }

    @Override
    protected Request buildRequest(RequestBody requestBody) {
        return builder.post(requestBody).build();
    }
}
