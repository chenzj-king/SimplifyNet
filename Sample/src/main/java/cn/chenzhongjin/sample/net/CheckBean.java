/*
 * Copyright (c) 2016  DreamLiner Studio
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
 */

package cn.chenzhongjin.sample.net;

import java.util.Map;

/**
 * @author chenzj
 * @Title: CheckBean
 * @Description: 类的描述 -
 * @date 2016/7/27 23:49
 * @email admin@chenzhongjin.cn
 */
public class CheckBean {

    private boolean isAllow;
    private Map<String, String> params;

    public CheckBean(boolean isAllow, Map<String, String> params) {
        this.isAllow = isAllow;
        this.params = params;
    }

    public boolean isAllow() {
        return isAllow;
    }

    public Map<String, String> getParams() {
        return params;
    }
}
