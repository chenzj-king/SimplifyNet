package com.dreamliner.simplifyokhttp.callback;

import android.text.TextUtils;

import com.dreamliner.simplifyokhttp.utils.GsonUtil;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

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
        if (TextUtils.isEmpty(bodyString))
            bodyString = this.mResponse.body().string();
        return bodyString;
    }

    public Reader getResponseBodyToReader() throws IOException {
        return this.mResponse.body().charStream();
    }

    public <T> Object getResponseBodyStringToObject(T type) throws Exception {
        if (TextUtils.isEmpty(bodyString))
            bodyString = this.mResponse.body().string();
        Gson gson = new Gson();
        return GsonUtil.fromJson(bodyString, (Class<T>) type);
        //return gson.fromJson(bodyString, (Class<T>) type);
    }

    public <T> Object getResponseBodyReaderToObject(T type) throws Exception {
        Reader reader = this.mResponse.body().charStream();
        Gson gson = new Gson();
        return GsonUtil.fromJson(reader, (Class<T>) type);
        //return gson.fromJson(reader, type);
    }

    public void filterResponse() {
    }
}
