/*
 *
 * Copyright (c) 2015 [admin@chenzhongjin | chenzhongjin@vip.qq.com]
 *
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
 *
 */

package cn.chenzhongjin.simplifynet.httputil;

import com.google.gson.Gson;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @author chenzj
 * @Title: BaseResponse
 * @Description: 网络请求回来的处理处理类.配合Gson来jsonStringToBean
 * @date 2015/11/7
 * @email admin@chenzhongjin.cn
 */
public class BaseResponse {
    private Response mResponse;

    public BaseResponse(Response response) {
        this.mResponse = response;
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
        return this.mResponse.body().string();
    }

    public Reader getResponseBodyToReader() throws IOException {
        return this.mResponse.body().charStream();
    }

    public Object getResponseBodyStringToObject(Type type) throws Exception {
        String str = this.mResponse.body().string();
        Gson gson = new Gson();
        return gson.fromJson(str, type);
    }

    public Object getResponseBodyReaderToObject(Type type) throws Exception {
        Reader reader = this.mResponse.body().charStream();
        Gson gson = new Gson();
        return gson.fromJson(reader, type);
    }

    public void filterResponse() {
    }
}

