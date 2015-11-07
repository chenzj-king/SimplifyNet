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

package cn.chenzhongjin.simplifynet.datatrasfer;

import android.text.TextUtils;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import cn.chenzhongjin.simplifynet.httputil.BaseCall;
import cn.chenzhongjin.simplifynet.httputil.BaseResponse;
import cn.chenzhongjin.simplifynet.httputil.DreamLinerException;
import cn.chenzhongjin.simplifynet.httputil.IHttpCallBack;

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
            public void onResponse(Response response) {
                BaseResponse basicResponse = new BaseResponse(response);
                if (basicResponse.getStatusCode() == 200) {
                    //callback request success
                    callback.onResponse(response);
                } else {
                    //no success you can do something in this
                }
            }

            public void onFailure(int errorCode, String errorMessage) {
                callback.onFailure(errorCode, errorMessage);
            }
        });
    }
}
