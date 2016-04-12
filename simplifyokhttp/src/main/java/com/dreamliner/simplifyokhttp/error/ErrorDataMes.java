package com.dreamliner.simplifyokhttp.error;

import com.google.gson.annotations.SerializedName;

/**
 * @author chenzj
 * @Title: ErrorDataMes
 * @Description: 类的描述 - 服务器请求返回的错误状态实体
 * @date 2016/3/19 19:37
 * @email admin@chenzhongjin.cn
 */
public class ErrorDataMes {

    @SerializedName("errNum")
    private int err;
    @SerializedName("errMsg")
    private String msg;

    public void setErr(int err) {
        this.err = err;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getErr() {
        return err;
    }

    public String getMsg() {
        return msg;
    }

    @Override
    public String toString() {
        return "ErrorDataMes{" +
                "err=" + err +
                ", msg='" + msg + '\'' +
                '}';
    }
}

