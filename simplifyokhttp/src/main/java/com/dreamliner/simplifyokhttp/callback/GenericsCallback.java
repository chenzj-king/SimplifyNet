package com.dreamliner.simplifyokhttp.callback;

import com.dreamliner.simplifyokhttp.error.ErrorDataMes;
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

        if (entityClass == String.class) {
            return (T) baseResponse.getResponseBodyToString();
        }

        final ErrorDataMes dataErrorMes = parseResponseHandler(baseResponse);

        // TODO: 2016/4/10 应该根据自己服务器的错误定义来进行回调到onEror/onSuccess.
        if (null != dataErrorMes) {
            if (dataErrorMes.getErr() != 0 && !dataErrorMes.getMsg().equals("success")) {
                throw new DreamLinerException(dataErrorMes.getErr(), dataErrorMes.getMsg());
            }
        } else {
            throw new DreamLinerException(ErrorCode.EXCHANGE_DATA_ERROR, "解释数据错误");
        }

        T bean = GsonUtil.getGson().fromJson(baseResponse.getResponseBodyToString(), entityClass);
        if (null == bean) {
            throw new DreamLinerException(ErrorCode.EXCHANGE_DATA_ERROR, mErrMes);
        }
        return bean;
    }


    public ErrorDataMes parseResponseHandler(BaseResponse basicResponse) {
        try {
            return (ErrorDataMes) GsonUtil.fromJsonToObj(basicResponse.getResponseBodyToString(), ErrorDataMes.class);
        } catch (Exception dataErrorMes) {
            //理论上不会走到这里.因为上一层pare的时候jsonStr都可以是正常的.
            return null;
        }
    }

}

