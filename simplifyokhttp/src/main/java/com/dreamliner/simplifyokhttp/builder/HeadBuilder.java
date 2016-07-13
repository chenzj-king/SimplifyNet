package com.dreamliner.simplifyokhttp.builder;

import com.dreamliner.simplifyokhttp.OkHttpUtils;
import com.dreamliner.simplifyokhttp.request.OtherRequest;
import com.dreamliner.simplifyokhttp.request.RequestCall;

/**
 * @author chenzj
 * @Title: HeadBuilder
 * @Description: 类的描述 -头部建造器
 * @date 2016/3/19 18:45
 * @email admin@chenzhongjin.cn
 */
public class HeadBuilder extends GetBuilder {
    @Override
    public RequestCall build() {
        return new OtherRequest(null, null, OkHttpUtils.METHOD.HEAD, url, tag, params, headers, id).build();
    }
}
