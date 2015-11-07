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

package cn.chenzhongjin.simplifynet.ui.activity.presenter;

import android.content.Context;

/**
 * @author chenzj
 * @Title: IWeatherPresenter
 * @Description: 天气presenter接口
 * @date
 * @email admin@chenzhongjin.cn
 */
public interface IWeatherPresenter {
    void clear();

    void doSearchWeather(Context context, String cityName);

}

