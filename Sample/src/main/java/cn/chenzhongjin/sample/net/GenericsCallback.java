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

import android.support.annotation.NonNull;

import com.dreamliner.simplifyokhttp.callback.BaseResponse;
import com.dreamliner.simplifyokhttp.callback.HttpCallBack;
import com.dreamliner.simplifyokhttp.utils.DreamLinerException;
import com.dreamliner.simplifyokhttp.utils.ErrorCode;
import com.dreamliner.simplifyokhttp.utils.GsonUtil;

import java.lang.reflect.ParameterizedType;

import cn.chenzhongjin.sample.entity.ErrorDataMes;

/**
 * @author chenzj
 * @Title: GenericsCallback
 * @Description: 类的描述 -
 * @date 2016/7/14 09:05
 * @email admin@chenzhongjin.cn
 */
public abstract class GenericsCallback<T> extends HttpCallBack<T> {

    private String mErrMes;

    public GenericsCallback() {
    }

    public GenericsCallback(@NonNull String errMes) {
        mErrMes = errMes;
    }

    public void setErrMes(String errMes) {
        mErrMes = errMes;
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

        T bean = null;
        try {
            bean = GsonUtil.getGson().fromJson(baseResponse.getResponseBodyToString(), entityClass);
        } catch (Exception ex) {
            throw new DreamLinerException(ErrorCode.EXCHANGE_DATA_ERROR, mErrMes);
        }
        return bean;
    }


    public ErrorDataMes parseResponseHandler(BaseResponse basicResponse) {
        try {
            return GsonUtil.fromJson(basicResponse.getResponseBodyToString(), ErrorDataMes.class);
        } catch (Exception dataErrorMes) {
            return null;
        }
    }
}

