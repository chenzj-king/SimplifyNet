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

import android.os.Handler;

import java.util.concurrent.Executor;

import cn.chenzhongjin.simplifynet.datatrasfer.DreamLinerResponse;
import cn.chenzhongjin.simplifynet.datatrasfer.IDataCallBack;

/**
 * @author chenzj
 * @Title: ExecutorDelivery
 * @Description: 执行数据请求的callback
 * @date 2015/11/7
 * @email admin@chenzhongjin.cn
 */
public class ExecutorDelivery {
    private final Executor mResponsePoster;

    public ExecutorDelivery(final Handler handler) {
        this.mResponsePoster = new Executor() {
            public void execute(Runnable command) {
                handler.post(command);
            }
        };
    }

    public <T extends DreamLinerResponse> void postSuccess(IDataCallBack<T> callback, T t) {
        this.mResponsePoster.execute(new ExecutorDelivery.ResponseDeliveryRunnable(0, t, callback));
    }

    public <T extends DreamLinerResponse> void postError(int code, String message, IDataCallBack<T> callback) {
        this.mResponsePoster.execute(new ExecutorDelivery.ResponseDeliveryRunnable(1, code, message, (DreamLinerResponse) null,
                callback));
    }

    private class ResponseDeliveryRunnable<T extends DreamLinerResponse> implements Runnable {
        private int code;
        private String message;
        private IDataCallBack<T> callback;
        private T t;
        private int postCode;

        public ResponseDeliveryRunnable(int code, int postCode, String message, T t, IDataCallBack<T> callback) {
            this.code = code;
            this.postCode = postCode;
            this.message = message;
            this.callback = callback;
            this.t = t;
        }

        public ResponseDeliveryRunnable(int code, T t, IDataCallBack<T> callback) {
            this.callback = callback;
            this.t = t;
            this.code = code;
        }

        public void run() {
            if (this.postCode == 0) {
                this.callback.onSuccess(this.t);
            } else if (this.postCode == 1) {
                this.callback.onError(this.code, this.message);
            }

        }
    }
}


