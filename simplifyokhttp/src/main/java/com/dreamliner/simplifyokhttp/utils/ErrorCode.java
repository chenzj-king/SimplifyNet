package com.dreamliner.simplifyokhttp.utils;

/**
 * @author chenzj
 * @Title: ErrorCode
 * @Description: 类的描述 - 各种错误状态码的定义
 * @date 2016/3/19 18:45
 * @email admin@chenzhongjin.cn
 */
public class ErrorCode {

    public final static int NET_DISABLE = -100; //网络没有开启
    public final static int NO_INTERNET = -101; //开着wifi/2g/3g/4g/但是没有网络信号
    public final static int NO_LOGIN = -108;//没有登录.token/userid被清空
    public final static int ERROR_PARMS = -109;//生成request的时候.parms=null/""引发的异常

    public final static int SOCKET_EXCEPTION = -102; //断网/网络差/主动取消request引发的异常
    public final static int INTERRUPTED_IOEXCEPTION = -103;//超时异常
    public final static int OTHER_IOEXCEPTION = -104;//除了上述的其他IO异常
    public final static int RUNTIME_EXCEPTION = -105;// 当okhttp ResponeCode!=200的时候引发的自定义RunTimeException

    public final static int SERVER_CUSTOM_ERROR = -106;//解释服务器返回无效请求的状态码
    public final static int EXCHANGE_DATA_ERROR = -107;//Gson解释JsonString引发的异常
}
