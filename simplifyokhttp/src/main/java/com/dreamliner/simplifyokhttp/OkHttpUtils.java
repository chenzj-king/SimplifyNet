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
import com.dreamliner.simplifyokhttp.callback.DataCallBack;
import com.dreamliner.simplifyokhttp.callback.HttpCallBack;
import com.dreamliner.simplifyokhttp.error.ErrorDataMes;
import com.dreamliner.simplifyokhttp.request.RequestCall;
import com.dreamliner.simplifyokhttp.utils.DreamLinerException;
import com.dreamliner.simplifyokhttp.utils.ErrorCode;
import com.dreamliner.simplifyokhttp.utils.GsonUtil;
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

    public <T> void postError(int errorCode, String errorMes, DataCallBack<T> callBack) {
        mPlatform.execute(new ResponseDeliveryRunnable<T>(errorCode, errorMes, callBack));
    }

    private class ResponseDeliveryRunnable<T> implements Runnable {

        private int erroCode;
        private String errorMessage;
        private DataCallBack<T> callback;

        public ResponseDeliveryRunnable(int erroCode, String errorMessage, DataCallBack<T> callback) {
            this.erroCode = erroCode;
            this.errorMessage = errorMessage;
            this.callback = callback;
        }

        @Override
        public void run() {
            callback.onError(erroCode, errorMessage);
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
                    sendSuccessResultCallback(object, baseResponse, call, finalHttpCallBack);
                } catch (Exception e) {
                    sendFailResultCallback(call, e, finalHttpCallBack);
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
                    if (exception instanceof SocketException) {
                        if (call.isCanceled()) {
                            Log.i("simplifyokhttp", "user finish Act/Fra cancel the request");
                        } else {
                            httpCallBack.onError(ErrorCode.SOCKET_EXCEPTION, "连接服务器失败，请重试！", call, exception);
                        }
                    } else if (exception instanceof InterruptedIOException) {
                        httpCallBack.onError(ErrorCode.INTERRUPTED_IOEXCEPTION, "请求失败，请重试！", call, exception);
                    } else {
                        httpCallBack.onError(ErrorCode.OTHER_IOEXCEPTION, "连接服务器失败，请重试！", call, exception);
                    }
                } else if (exception instanceof DreamLinerException) {
                    //这是上层parseNetworkResponse的时候.如果gson解释有问题.就会抛出这个异常.就可以走onError回调
                    httpCallBack.onError(((DreamLinerException) exception).getErrorCode(),
                            ((DreamLinerException) exception).getErrorMessage(), call, exception);
                } else if (exception instanceof AndroidRuntimeException) {
                    //请求状态码非200的提示
                    httpCallBack.onError(ErrorCode.RUNTIME_EXCEPTION, "连接服务器失败，返回的状态码不为200，请重试！", call, exception);
                } else if (exception instanceof RuntimeException) {
                    httpCallBack.onError(ErrorCode.RUNTIME_EXCEPTION, "parseNetworkResponse抛出异常，请重试！", call, exception);
                }
                httpCallBack.onAfter();
            }
        });
    }

    public void sendSuccessResultCallback(final Object object, final BaseResponse baseResponse, final Call call,
                                          final HttpCallBack httpCallBack) {
        if (httpCallBack == null) return;

        final ErrorDataMes dataErrorMes = parseResponseHandler(baseResponse);

        mPlatform.execute(new Runnable() {
            @Override
            public void run() {

                try {
                    // TODO: 2016/4/10 应该根据自己服务器的错误定义来进行回调到onEror/onSuccess.
                    if (null != dataErrorMes) {
                        if (dataErrorMes.getErr() == 0 && dataErrorMes.getMsg().equals("success")) {
                            //服务器返回成功的状态码和信息
                            httpCallBack.onResponse(object);
                        } else {
                            //正常的服务器错误状态码
                            httpCallBack.onError(ErrorCode.SERVER_CUSTOM_ERROR, dataErrorMes.getMsg(), call, null);
                        }
                    } else {
                        //回来的报文不是规范Json导致无法用Gson解释catch
                        httpCallBack.onError(ErrorCode.EXCHANGE_DATA_ERROR, "解释数据错误", null, null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    //理论上以后都不会走这边.因为在上层parseNetworkResponse的时候.如果responseResult不是标准json.
                    //则会抛出SqEx.然后会直接走sendOnError
                    //这里也是为了防止在执行onResponse的时候操作不当导致crash
                    httpCallBack.onError(ErrorCode.EXCHANGE_DATA_ERROR, e.getMessage(), call, e);
                } finally {
                    httpCallBack.onAfter();
                }
            }
        });
    }

    public static ErrorDataMes parseResponseHandler(BaseResponse basicResponse) {
        try {
            return (ErrorDataMes) GsonUtil.fromJsonToObj(basicResponse.getResponseBodyToString(), ErrorDataMes.class);
        } catch (Exception dataErrorMes) {
            //理论上不会走到这里.因为上一层pare的时候jsonStr都可以是正常的.
            return null;
        }
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



