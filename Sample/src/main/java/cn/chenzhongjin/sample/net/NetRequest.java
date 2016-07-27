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

package cn.chenzhongjin.sample.net;

import android.support.annotation.StringRes;

import com.dreamliner.simplifyokhttp.OkHttpUtils;
import com.dreamliner.simplifyokhttp.builder.PostFormBuilder;
import com.dreamliner.simplifyokhttp.callback.HttpCallBack;
import com.dreamliner.simplifyokhttp.request.RequestCall;
import com.dreamliner.simplifyokhttp.utils.ErrorCode;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.chenzhongjin.sample.AppContext;
import cn.chenzhongjin.sample.R;
import cn.chenzhongjin.sample.entity.Weather;
import cn.chenzhongjin.sample.net.utils.NetUtils;

/**
 * @author chenzj
 * @Title: NetRequest
 * @Description: 访问请求类.拼装url并且访问成功之后返回结果.各种层次的错误都回调返回对应的状态码和错误信息
 * @date 2015/11/7
 * @email admin@chenzhongjin.cn
 */
public class NetRequest {

    private static final String TAG = "NetRequest";

    public static String getString(@StringRes int resId) {
        return AppContext.getInstance().getString(resId);
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

    public static <T> boolean checkNetStatus(HttpCallBack<T> callback) {
        if (!NetUtils.isNetworkAvailable(AppContext.getInstance())) {
            OkHttpUtils.getInstance().postError(ErrorCode.NET_DISABLE, "请检查你的网络状态!", callback);
            return false;
        }
        return true;
    }

    public static <T> Map<String, String> addCommonParams(Map<String, String> params, HttpCallBack<T> callback) {

        //这里进行必要参数的校验.例如token之类的不能为空
        boolean testError = false;
        if (testError) {
            OkHttpUtils.getInstance().postError(ErrorCode.NO_LOGIN, "未登录", callback);
            return null;
        }
        params.put("token", "12345.上山打老虎");
        //添加服务器可能需要统计/校验的参数
        /*
        map.put("app_key", getInstanse().getAppKey());
        map.put("device_id", this.getDeviceId());
        map.put("pack_id", getInstanse().getPackId());
        map.put("sdk_version", getInstanse().getSdkVersion());
        map.put("client_os_type", String.valueOf(getInstanse().getClientOsType()));
        */
        return params;
    }

    private static CheckBean checkNetAndAddParams(Map<String, String> specificParams, HttpCallBack callback) {

        if (!checkNetStatus(callback)) {
            //这里进行网络状态判断.無网络直接回调onError.return该请求
            return new CheckBean(false, specificParams);
        }
        specificParams = addCommonParams(specificParams, callback);

        //这里可以进行是否登录的校验.如果没有Token就回调onEror.并且return该请求(类似登录接口/查询无需登录权限的业务就无需调用该方法)
        return new CheckBean(null != specificParams, specificParams);
    }

    public static void get(String url, Map<String, String> params, Object object, GenericsCallback callback) {
        if (!checkNetStatus(callback)) {
            return;
        }
        try {
            OkHttpUtils.get().url(url).params(params).tag(object).build().execute(callback);
        } catch (Exception ex) {
            ex.printStackTrace();
            OkHttpUtils.getInstance().postError(ErrorCode.ERROR_PARMS, "请求参数本地异常", callback);
        }
    }

    public static void getAddParams(String url, Map<String, String> specificParams, Object tag, GenericsCallback<Weather> callback) {
        CheckBean checkBean = checkNetAndAddParams(specificParams, callback);
        if (!checkBean.isAllow()) {
            return;
        }
        try {
            OkHttpUtils.get().url(url)
                    .addHeader("apikey", "15f0d14ed33720b6b73ec8a3f7bb4d46")
                    .params(checkBean.getParams())
                    .tag(tag).build()
                    .execute(callback);
        } catch (Exception e) {
            e.printStackTrace();
            OkHttpUtils.getInstance().postError(ErrorCode.ERROR_PARMS, "请求参数本地异常", callback);
        }
    }

    public static void post(String url, Map<String, String> params, Object object, GenericsCallback callback) {
        if (!checkNetStatus(callback)) {
            return;
        }
        try {
            callback.setErrMes("解释登录信息失败");
            OkHttpUtils.post().url(url).params(params).tag(object).build().execute(callback);
        } catch (Exception ex) {
            ex.printStackTrace();
            OkHttpUtils.getInstance().postError(ErrorCode.ERROR_PARMS, "请求参数本地异常", callback);
        }
    }

    public static void postParm(String url, Map<String, String> params, Object object, GenericsCallback callback) {
        CheckBean checkBean = checkNetAndAddParams(params, callback);
        if (!checkBean.isAllow()) {
            return;
        }
        try {
            OkHttpUtils.post().url(url).params(checkBean.getParams()).tag(object).build().execute(callback);
        } catch (Exception ex) {
            ex.printStackTrace();
            OkHttpUtils.getInstance().postError(ErrorCode.ERROR_PARMS, "请求参数本地异常", callback);
        }
    }

    public static void postParmAndFile(String url, Map<String, String> params, File file, Object object, GenericsCallback callback) {
        CheckBean checkBean = checkNetAndAddParams(params, callback);
        if (!checkBean.isAllow()) {
            return;
        }
        try {
            //注意这里的mFile作为key.要看自己后台配合取什么
            OkHttpUtils.post().url(url).params(checkBean.getParams()).addFile("mFile", file.getName(), file).tag(object).build()
                    .execute(callback);
        } catch (Exception ex) {
            ex.printStackTrace();
            OkHttpUtils.getInstance().postError(ErrorCode.ERROR_PARMS, "请求参数本地异常", callback);
        }
    }

    public static void postFiles(String url, List<File> files, List<String> textInputs, Object tag, GenericsCallback callback) {
        if (!checkNetStatus(callback)) {
            return;
        }
        try {
            PostFormBuilder postFormBuilder = OkHttpUtils.post().url(url);
            if (null != files) {
                for (int i = 0; i < files.size(); i++) {
                    //根据业务逻辑定义key
                    postFormBuilder.addFile("file" + i, files.get(i).getName(), files.get(i));
                }
            }
            if (null != textInputs) {
                for (int i = 0; i < textInputs.size(); i++) {
                    //根据业务逻辑定义key
                    postFormBuilder.addText("text" + i, textInputs.get(i));
                }
            }
            postFormBuilder.build().execute(callback);
        } catch (Exception ex) {
            ex.printStackTrace();
            OkHttpUtils.getInstance().postError(ErrorCode.ERROR_PARMS, "请求参数本地异常", callback);
        }
    }

    public static void postJson(String url, String jsonStr, Object object, GenericsCallback callback) {
        if (!checkNetStatus(callback)) {
            return;
        }
        try {
            OkHttpUtils.postJson().url(url).addJsonStr(jsonStr).tag(object).build().execute(callback);
        } catch (Exception ex) {
            ex.printStackTrace();
            OkHttpUtils.getInstance().postError(ErrorCode.ERROR_PARMS, "请求参数本地异常", callback);
        }
    }

    public static RequestCall getHeadJsonZip(String url, String jsonStr, Object tag, GenericsCallback callback) {
        if (!checkNetStatus(callback)) {
            return null;
        }
        try {
            RequestCall requestCall = OkHttpUtils.postJsonZip().url(url)
                    .headers(getGzipGetHeaders()).addJsonStr(jsonStr).tag(tag).build();
            requestCall.execute(callback);
            return requestCall;
        } catch (Exception ex) {
            ex.printStackTrace();
            OkHttpUtils.getInstance().postError(ErrorCode.ERROR_PARMS, getString(R.string.netrequest_error_parms), callback);
            return null;
        }
    }

    public static RequestCall postHeadJsonZip(String url, String jsonStr, Object tag, GenericsCallback callback) {
        if (!checkNetStatus(callback)) {
            return null;
        }
        try {
            RequestCall requestCall = OkHttpUtils.postJsonZip().url(url).addJsonStr(jsonStr).headers(getGzipPostHeaders())
                    .tag(tag).build();
            requestCall.execute(callback);
            return requestCall;
        } catch (Exception ex) {
            ex.printStackTrace();
            OkHttpUtils.getInstance().postError(ErrorCode.ERROR_PARMS, getString(R.string.netrequest_error_parms), callback);
            return null;
        }
    }

    public static void getWeatherMsg(Map<String, String> specificParams, Object object, final GenericsCallback<Weather> callback) {
        callback.setErrMes("解释天气数据失败");
        getAddParams(HttpUrl.BASE_WEATHER_URL, specificParams, object, callback);
    }
}

