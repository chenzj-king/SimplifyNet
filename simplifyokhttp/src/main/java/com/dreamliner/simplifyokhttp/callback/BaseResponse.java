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

package com.dreamliner.simplifyokhttp.callback;

import android.text.TextUtils;

import com.dreamliner.simplifyokhttp.utils.GsonUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.zip.GZIPInputStream;

import okhttp3.Response;

/**
 * @author chenzj
 * @Title: GsonUtil
 * @Description: 类的描述 - 网络请求回来的处理处理类.配合Gson来jsonStringToBean
 * @date 2016/3/19 18:45
 * @email admin@chenzhongjin.cn
 */
public class BaseResponse {

    private Response mResponse;
    private String bodyString;
    private Reader reader;

    public BaseResponse(Response response) {
        this.mResponse = response;
    }

    public Response getResponse() {
        return mResponse;
    }

    public int getStatusCode() {
        return this.mResponse.code();
    }

    public String getStatusMessage() {
        return this.mResponse.message();
    }

    public List<String> getHeader(String key) {
        return this.mResponse.headers(key);
    }

    public String getResponseBodyToString() throws IOException {
        if (TextUtils.isEmpty(bodyString)) {
            if (mResponse.headers().names().contains("Content-Encoding")
                    && mResponse.headers().get("Content-Encoding").equals("gzip")) {
                InputStream in = new GZIPInputStream(mResponse.body().byteStream());
                ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
                int len;
                byte[] buffer = new byte[1024];
                while ((len = in.read(buffer)) != -1) {
                    arrayOutputStream.write(buffer, 0, len);
                }
                in.close();
                arrayOutputStream.close();
                bodyString = new String(arrayOutputStream.toByteArray(), "utf-8");
            } else {
                bodyString = this.mResponse.body().string();
            }
        }
        return bodyString;
    }

    public Reader getRbToReader() throws IOException {
        return this.mResponse.body().charStream();
    }

    public <T> T getRbStringToObject(Class<T> classOfT) throws IOException {
        return GsonUtil.fromJson(bodyString, classOfT);
    }

    public <T> T getRbStringToObject(Type type) throws IOException {
        if (TextUtils.isEmpty(bodyString))
            getResponseBodyToString();
        return GsonUtil.fromJson(bodyString, type);
    }

    public <T> T getRbReaderToObject(Type type) throws IOException {
        Reader reader = this.mResponse.body().charStream();
        return GsonUtil.fromJson(reader, type);
    }

    public void filterResponse() {
    }
}
