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
 * @Title: DataErrorCategory
 * @Description: 后台如果有协定好请求失败的话.可以定制
 * @date 2015/11/7
 * @email admin@chenzhongjin.cn
 */
public class DataErrorCategory {
    @SerializedName("error_no")
    private int mErrorNo;
    @SerializedName("error_code")
    private String mErrorCode;
    @SerializedName("error_desc")
    private String mErrorDesc;

    public DataErrorCategory() {
    }

    public int getErrorNo() {
        return this.mErrorNo;
    }

    public void setErrorNo(int mErrorNo) {
        this.mErrorNo = mErrorNo;
    }

    public String getErrorCode() {
        return this.mErrorCode;
    }

    public void setErrorCode(String mErrorCode) {
        this.mErrorCode = mErrorCode;
    }

    public String getErrorDesc() {
        return this.mErrorDesc;
    }

    public void setErrorDesc(String mErrorDesc) {
        this.mErrorDesc = mErrorDesc;
    }
}

