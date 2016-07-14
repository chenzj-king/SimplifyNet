package com.dreamliner.simplifyokhttp.callback;

/**
 * @author chenzj
 * @Title: IGenericsSerializator
 * @Description: 类的描述 -
 * @date 2016/7/14 09:05
 * @email admin@chenzhongjin.cn
 */
public interface IGenericsSerializator {
    <T> T transform(String response, Class<T> classOfT);
}  

