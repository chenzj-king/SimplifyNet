package com.dreamliner.simplifyokhttp.utils;

/**
 * @author chenzj
 * @Title: Exceptions
 * @Description: 类的描述 -
 * @date 2016/3/19 18:45
 * @email admin@chenzhongjin.cn
 */
public class Exceptions {
    public static void illegalArgument(String msg, Object... params) {
        throw new IllegalArgumentException(String.format(msg, params));
    }
}
