/*
 *
 * Copyright (c) 2015 [admin@chenzhongjin | chenzhongjin@vip.qq.com]
 *
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
 *
 */

package cn.chenzhongjin.sample.lib.datatrasfer;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import cn.chenzhongjin.sample.lib.httputil.BaseCall;
import cn.chenzhongjin.sample.lib.httputil.BaseResponse;
import cn.chenzhongjin.sample.lib.httputil.DreamLinerException;
import cn.chenzhongjin.sample.lib.httputil.IHttpCallBack;


/**
 * @author chenzj
 * @Title: OtpBaseCall
 * @Description:
 * @date 2015/11/7
 * @email admin@chenzhongjin.cn
 */
public class OtpBaseCall {
    public OtpBaseCall() {
    }

    public static String doSync(Request request) throws
            DreamLinerException {
        String responseString = null;

        try {
            Response e = BaseCall.doSync(request);
            BaseResponse basicResponse = new BaseResponse(e);
            if (e.code() == 200) {
                responseString = e.body().string();
            } else {

            }
            return responseString;
        } catch (IOException ioException) {
            if (TextUtils.isEmpty(ioException.getMessage())) {
                throw new DreamLinerException(604, "http error");
            } else {
                throw new DreamLinerException(604, ioException.getMessage());
            }
        }
    }

    public static void doAsync(Request request, final IHttpCallBack callback) {
        BaseCall.doAsync(request, new IHttpCallBack() {
            public void onResponse(BaseResponse baseResponse) {
                if (baseResponse.getStatusCode() == 200) {
                    //callback request success but you should judge the data is or not a erroMes
                    DataErrorMes dataErrorMes = parseResponseHandler(baseResponse);

                    if (null != dataErrorMes && !TextUtils.isEmpty(dataErrorMes.getErrMsg())) {
                        callback.onFailure(dataErrorMes.getErrNum(), dataErrorMes.getErrMsg());
                    } else {
                        callback.onResponse(baseResponse);
                    }
                } else {
                    //no success you can do something in this
                    callback.onFailure(baseResponse.getStatusCode(), "请求失败");
                }
            }

            public void onFailure(int errorCode, String errorMessage) {
                callback.onFailure(errorCode, errorMessage);
            }
        });
    }

    public static DataErrorMes parseResponseHandler(BaseResponse basicResponse) {
        Gson gson = new Gson();
        try {
            DataErrorMes dataErrorMes = (DataErrorMes) gson.fromJson(basicResponse.getResponseBodyToString(), DataErrorMes.class);
            return dataErrorMes;
        } catch (Exception dataErrorMes) {
            return null;
        }
    }
}
