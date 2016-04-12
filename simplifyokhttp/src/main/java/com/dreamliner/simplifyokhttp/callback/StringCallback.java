package com.dreamliner.simplifyokhttp.callback;

import java.io.IOException;

/**
 * @author chenzj
 * @Title: StringCallback
 * @Description: 类的描述 - 字符串的回调
 * @date 2016/3/19 18:45
 * @email admin@chenzhongjin.cn
 */
public abstract class StringCallback extends HttpCallBack<String> {

    @Override
    public String parseNetworkResponse(BaseResponse baseResponse) throws IOException {
        return baseResponse.getResponse().body().string();
    }

}
