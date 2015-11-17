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

import com.google.gson.annotations.SerializedName;

/**
 * @author chenzj
 * @Title: DataErrorMes
 * @Description: 后台如果有协定好请求失败的话.可以定制
 * @date 2015/11/7
 * @email admin@chenzhongjin.cn
 */
public class DataErrorMes {
    @SerializedName("errorNo")
    private int mErrorNo;
    @SerializedName("errorCode")
    private String mErrorCode;
    @SerializedName("errorMes")
    private String mErrorMes;

    public DataErrorMes() {
    }

    public int getmErrorNo() {
        return mErrorNo;
    }

    public void setmErrorNo(int mErrorNo) {
        this.mErrorNo = mErrorNo;
    }

    public String getmErrorCode() {
        return mErrorCode;
    }

    public void setmErrorCode(String mErrorCode) {
        this.mErrorCode = mErrorCode;
    }

    public String getmErrorMes() {
        return mErrorMes;
    }

    public void setmErrorMes(String mErrorMes) {
        this.mErrorMes = mErrorMes;
    }
}

