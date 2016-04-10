package com.dreamliner.simplifyokhttp.builder;

import java.util.Map;

/**
 * @author chenzj
 * @Title: HasParamsable
 * @Description: 类的描述 - 添加参数接口
 * @date 2016/3/19 18:45
 * @email admin@chenzhongjin.cn
 */
public interface HasParamsable {

    public abstract OkHttpRequestBuilder params(Map<String, String> params);

    public abstract OkHttpRequestBuilder addParams(String key, String val);

}
