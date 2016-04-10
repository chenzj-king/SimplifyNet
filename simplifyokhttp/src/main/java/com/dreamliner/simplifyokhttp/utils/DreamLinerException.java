package com.dreamliner.simplifyokhttp.utils;

/**
 * @文件名: DreamLinerException
 * @功能描述: 自定义异常
 * @Create by chenzj on 2015-12-2 上午10:11:16
 * @email: chenzj@sq580.com
 * @修改记录:
 */
public class DreamLinerException extends Exception {
    
    private int mErrorCode;
    private String mErrorMessage;

    public DreamLinerException(int errorCode, String errorMessage) {
        this.mErrorCode = errorCode;
        this.mErrorMessage = errorMessage;
    }

    public int getErrorCode() {
        return this.mErrorCode;
    }

    public void setErrorCode(int mErrorCode) {
        this.mErrorCode = mErrorCode;
    }

    public String getErrorMessage() {
        return this.mErrorMessage;
    }

    public void setErrorMessage(String mErrorMessage) {
        this.mErrorMessage = mErrorMessage;
    }
}
