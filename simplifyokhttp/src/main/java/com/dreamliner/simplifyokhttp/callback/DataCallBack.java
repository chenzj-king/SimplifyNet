package com.dreamliner.simplifyokhttp.callback;

/**
 * @author chenzj
 * @Title: DataCallBack
 * @Description: 类的描述 - 最终解释到的数据回调
 * @date 2016/3/19 18:45
 * @email admin@chenzhongjin.cn
 */
public interface DataCallBack<T> {

    void onSuccess(T bean);

    void onError(int errorCode, String errorMes);
}
