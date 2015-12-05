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

package cn.chenzhongjin.sample.lib.httputil;

import android.text.TextUtils;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * @author chenzj
 * @Title: BaseCall
 * @Description: 最基础的okhttp请求类
 * @date 2015/11/7
 * @email admin@chenzhongjin.cn
 */
public class BaseCall {

    private static OkHttpClient mOkHttpClient = new OkHttpClient();

    public BaseCall() {
    }

    public static OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    public static Response doSync(OkHttpClient client, Request request) throws IOException {
        return client.newCall(request).execute();
    }

    public static Response doSync(Request request) throws IOException {
        return getOkHttpClient().newCall(request).execute();
    }

    public static void doAsync(OkHttpClient client, Request request, final IHttpCallBack callback) {
        client.newCall(request).enqueue(new Callback() {
            public void onResponse(Response response) throws IOException {
                callback.onResponse(new BaseResponse(response));
            }

            public void onFailure(Request request, IOException e) {
                if (TextUtils.isEmpty(e.getMessage())) {
                    callback.onFailure(604, "http error");
                } else {
                    callback.onFailure(604, e.getMessage());
                }

            }
        });
    }

    public static void doAsync(Request request, final IHttpCallBack callback) {
        getOkHttpClient().newCall(request).enqueue(new Callback() {
            public void onResponse(Response response) throws IOException {
                callback.onResponse(new BaseResponse(response));
            }

            public void onFailure(Request request, IOException e) {
                if (TextUtils.isEmpty(e.getMessage())) {
                    callback.onFailure(604, "http error");
                } else {
                    callback.onFailure(604, e.getMessage());
                }
            }
        });
    }
}
