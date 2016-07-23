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

package com.dreamliner.simplifyokhttp;

import android.util.AndroidRuntimeException;
import android.util.Log;

import com.dreamliner.simplifyokhttp.builder.GetBuilder;
import com.dreamliner.simplifyokhttp.builder.HeadBuilder;
import com.dreamliner.simplifyokhttp.builder.OtherRequestBuilder;
import com.dreamliner.simplifyokhttp.builder.PostFileBuilder;
import com.dreamliner.simplifyokhttp.builder.PostFormBuilder;
import com.dreamliner.simplifyokhttp.builder.PostJsonBuilder;
import com.dreamliner.simplifyokhttp.builder.PostJsonZipBuilder;
import com.dreamliner.simplifyokhttp.builder.PostStringBuilder;
import com.dreamliner.simplifyokhttp.callback.BaseResponse;
import com.dreamliner.simplifyokhttp.callback.HttpCallBack;
import com.dreamliner.simplifyokhttp.request.RequestCall;
import com.dreamliner.simplifyokhttp.utils.DreamLinerException;
import com.dreamliner.simplifyokhttp.utils.ErrorCode;
import com.dreamliner.simplifyokhttp.utils.Platform;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * @author chenzj
 * @Title: OkHttpUtils
 * @Description: 类的描述 - 对外开放的okhttp请求工具类
 * @date 2016/3/19 18:45
 * @email admin@chenzhongjin.cn
 */
public class OkHttpUtils {

    public static final long DEFAULT_MILLISECONDS = 10_000L;
    private volatile static OkHttpUtils mInstance;
    private OkHttpClient mOkHttpClient;
    private Platform mPlatform;

    public OkHttpUtils(OkHttpClient okHttpClient) {
        if (okHttpClient == null) {
            mOkHttpClient = new OkHttpClient();
        } else {
            mOkHttpClient = okHttpClient;
        }

        mPlatform = Platform.get();
    }


    public static OkHttpUtils initClient(OkHttpClient okHttpClient) {
        if (mInstance == null) {
            synchronized (OkHttpUtils.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpUtils(okHttpClient);
                }
            }
        }
        return mInstance;
    }

    public static OkHttpUtils getInstance() {
        return initClient(null);
    }

    public Executor getDelivery() {
        return mPlatform.defaultCallbackExecutor();
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    public <T> void postError(int errorCode, String errorMes, HttpCallBack<T> callBack) {
        mPlatform.execute(new ResponseDeliveryRunnable<T>(errorCode, errorMes, callBack, null, null));
    }

    private class ResponseDeliveryRunnable<T> implements Runnable {

        private int erroCode;
        private String errorMessage;
        private HttpCallBack<T> callback;
        private Call call;
        private Exception e;


        public ResponseDeliveryRunnable(int erroCode, String errorMessage, HttpCallBack<T> callback, Call call, Exception e) {
            this.erroCode = erroCode;
            this.errorMessage = errorMessage;
            this.callback = callback;
            this.call = call;
            this.e = e;
        }

        @Override
        public void run() {
            callback.onError(erroCode, errorMessage, call, e);
        }
    }

    public static Map<String, String> getGzipGetHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Encoding", "gzip");
        headers.put("Accept-Encoding", "gzip,deflate");
        return headers;
    }

    public static Map<String, String> getGzipPostHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Encoding", "gzip");
        return headers;
    }

    public static GetBuilder get() {
        return new GetBuilder();
    }

    public static PostStringBuilder postString() {
        return new PostStringBuilder();
    }

    public static PostFileBuilder postFile() {
        return new PostFileBuilder();
    }

    public static PostFormBuilder post() {
        return new PostFormBuilder();
    }

    public static PostJsonBuilder postJson() {
        return new PostJsonBuilder();
    }

    public static PostJsonZipBuilder postJsonZip() {
        return new PostJsonZipBuilder();
    }

    public static OtherRequestBuilder put() {
        return new OtherRequestBuilder(METHOD.PUT);
    }

    public static HeadBuilder head() {
        return new HeadBuilder();
    }

    public static OtherRequestBuilder delete() {
        return new OtherRequestBuilder(METHOD.DELETE);
    }

    public static OtherRequestBuilder patch() {
        return new OtherRequestBuilder(METHOD.PATCH);
    }

    public void execute(final RequestCall requestCall, HttpCallBack httpCallBack) {
        if (httpCallBack == null)
            httpCallBack = HttpCallBack.HttpCallBackDefault;
        final HttpCallBack finalHttpCallBack = httpCallBack;

        requestCall.getCall().enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                sendFailResultCallback(call, e, finalHttpCallBack);
            }

            @Override
            public void onResponse(final Call call, final Response response) {
                if (response.code() >= 400 && response.code() <= 599) {
                    try {
                        sendFailResultCallback(call, new AndroidRuntimeException(response.body().string()), finalHttpCallBack);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return;
                }

                try {
                    BaseResponse baseResponse = new BaseResponse(response);
                    Object object = finalHttpCallBack.parseNetworkResponse(baseResponse);
                    sendSuccessResultCallback(object, call, finalHttpCallBack);
                } catch (Exception e) {
                    sendFailResultCallback(call, e, finalHttpCallBack);
                } finally {
                    response.close();
                }

            }
        });
    }

    public void sendFailResultCallback(final Call call, final Exception exception, final HttpCallBack httpCallBack) {
        if (httpCallBack == null) return;

        mPlatform.execute(new Runnable() {
            @Override
            public void run() {

                if (exception instanceof IOException) {
                    if (call.isCanceled()) {
                        Log.i("simplifyokhttp", "user finish Act/Fra/Dialog cancel the request");
                    } else {
                        if (exception instanceof SocketException) {
                            httpCallBack.onError(ErrorCode.SOCKET_EXCEPTION, "连接服务器失败，请重试！", call, exception);
                        } else if (exception instanceof InterruptedIOException) {
                            httpCallBack.onError(ErrorCode.INTERRUPTED_IOEXCEPTION, "请求失败，请重试！", call, exception);
                        } else {
                            httpCallBack.onError(ErrorCode.OTHER_IOEXCEPTION, "连接服务器失败，请重试！", call, exception);
                        }
                    }
                } else if (exception instanceof AndroidRuntimeException) {

                    //请求状态码非200的提示
                    httpCallBack.onError(ErrorCode.RESPONSE_ERROR_CODE_EXCEPTION, "请求成功,但返回的状态码不为200，请重试！", call, exception);

                } else if (exception instanceof DreamLinerException) {

                    //判断到是服务器返回的异常
                    DreamLinerException dreamLinerException = (DreamLinerException) exception;
                    httpCallBack.onError(dreamLinerException.getErrorCode(), dreamLinerException.getErrorMessage(), call, exception);

                } else {
                    //在parseNetworkResponse引发的异常/onResponse的时候操作不当引发的异常
                    httpCallBack.onError(ErrorCode.RUNTIME_EXCEPTION, exception.getMessage(), call, exception);
                }
                httpCallBack.onAfter();
            }
        });
    }

    public void sendSuccessResultCallback(final Object object, final Call call, final HttpCallBack httpCallBack) {
        if (httpCallBack == null) return;

        mPlatform.execute(new Runnable() {
            @Override
            public void run() {

                try {
                    httpCallBack.onResponse(object);
                } catch (Exception e) {
                    e.printStackTrace();
                    //这里是为了防止在执行onResponse的时候操作不当导致crash
                    httpCallBack.onError(ErrorCode.EXCHANGE_DATA_ERROR, e.getMessage(), call, e);
                } finally {
                    httpCallBack.onAfter();
                }
            }
        });
    }

    public void cancelTag(Object tag) {
        for (Call call : mOkHttpClient.dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : mOkHttpClient.dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    public void setConnectTimeout(int timeout, TimeUnit units) {
        mOkHttpClient = getOkHttpClient().newBuilder()
                .connectTimeout(timeout, units)
                .build();
    }

    public void setReadTimeout(int timeout, TimeUnit units) {
        mOkHttpClient = getOkHttpClient().newBuilder()
                .readTimeout(timeout, units)
                .build();
    }

    public void setWriteTimeout(int timeout, TimeUnit units) {
        mOkHttpClient = getOkHttpClient().newBuilder()
                .writeTimeout(timeout, units)
                .build();
    }


    public static class METHOD {
        public static final String HEAD = "HEAD";
        public static final String DELETE = "DELETE";
        public static final String PUT = "PUT";
        public static final String PATCH = "PATCH";
    }
}



