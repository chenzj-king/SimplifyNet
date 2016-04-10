package com.dreamliner.simplifyokhttp.callback;

import java.io.IOException;

import okhttp3.Response;

/**
 * @author chenzj
 * @Title: StringCallback
 * @Description: 类的描述 - 字符串的回调
 * @date 2016/3/19 18:45
 * @email admin@chenzhongjin.cn
 */
public abstract class StringCallback extends HttpCallBack<String> {

    @Override
    public String parseNetworkResponse(Response response) throws IOException {
        return response.body().string();
    }

}
