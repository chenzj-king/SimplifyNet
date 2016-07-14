package com.dreamliner.simplifyokhttp.callback;

import com.dreamliner.simplifyokhttp.utils.DreamLinerException;
import com.dreamliner.simplifyokhttp.utils.ErrorCode;
import com.dreamliner.simplifyokhttp.utils.GsonUtil;

import java.lang.reflect.ParameterizedType;

import okhttp3.Call;

/**
 * @author chenzj
 * @Title: GenericsCallback
 * @Description: 类的描述 -
 * @date 2016/7/14 09:05
 * @email admin@chenzhongjin.cn
 */
public abstract class GenericsCallback<T> extends HttpCallBack<T> {

    private String mErrMes;
    private DataCallBack<T> mDataCallBack;

    public GenericsCallback(String errMes, DataCallBack<T> dataCallBack) {
        mErrMes = errMes;
        mDataCallBack = dataCallBack;
    }

    @Override
    public void onError(int errorCode, String errorMes, Call call, Exception e) {
        mDataCallBack.onError(errorCode, errorMes);
    }

    @Override
    public void onResponse(T response) {
        mDataCallBack.onSuccess(response);
    }

    @Override
    public T parseNetworkResponse(BaseResponse baseResponse) throws Exception {

        Class<T> entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

        T bean = GsonUtil.getGson().fromJson(baseResponse.getResponseBodyToString(), entityClass);
        if (null == bean) {
            throw new DreamLinerException(ErrorCode.EXCHANGE_DATA_ERROR, mErrMes);
        }
        return bean;
    }

}

