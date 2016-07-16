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

import okhttp3.Call;
import okhttp3.Request;

/**
 * @author chenzj
 * @Title: HttpCallBack
 * @Description: 类的描述 - okhttp的callback回调之后的回调处理抽象类
 * @date 2016/3/19 18:45
 * @email admin@chenzhongjin.cn
 */
public abstract class HttpCallBack<T> {

    public abstract void onError(int errorCode, String errorMes, Call call, Exception e);

    public abstract void onResponse(T response);

    /**
     * Thread Pool Thread
     *
     * @param baseResponse
     */
    public abstract T parseNetworkResponse(BaseResponse baseResponse) throws Exception;

    /**
     * UI Thread
     *
     * @param request
     */
    public void onBefore(Request request) {
    }

    /**
     * UI Thread
     *
     * @param
     */
    public void onAfter() {
    }

    /**
     * UI Thread
     *
     * @param progress
     */
    public void inProgress(float progress) {
    }

    public static HttpCallBack HttpCallBackDefault = new HttpCallBack() {

        @Override
        public Object parseNetworkResponse(BaseResponse baseResponse) throws Exception {
            return null;
        }

        @Override
        public void onError(int errorCode, String errorMes, Call call, Exception e) {

        }

        @Override
        public void onResponse(Object response) {

        }
    };

}