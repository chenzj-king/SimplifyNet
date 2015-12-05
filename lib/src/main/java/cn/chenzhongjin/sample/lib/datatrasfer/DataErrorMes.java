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

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * @author chenzj
 * @Title: DataErrorMes
 * @Description: 后台如果有协定好请求失败的话.可以定制
 * @date 2015/11/7
 * @email admin@chenzhongjin.cn
 */
public class DataErrorMes {

    // TODO: 2015/12/5 design you errorMes by youself
    @SerializedName("errNum")
    private int errNum;
    @SerializedName("errMsg")
    private String errMsg;
    @SerializedName("retData")
    private List<?> retData;

    public void setErrNum(int errNum) { this.errNum = errNum;}

    public void setErrMsg(String errMsg) { this.errMsg = errMsg;}

    public void setRetData(List<?> retData) { this.retData = retData;}

    public int getErrNum() { return errNum;}

    public String getErrMsg() { return errMsg;}

    public List<?> getRetData() { return retData;}
}

