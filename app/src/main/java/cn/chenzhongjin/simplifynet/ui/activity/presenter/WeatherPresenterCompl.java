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
import android.os.Handler;
import android.os.Looper;

import java.util.HashMap;
import java.util.Map;

import cn.chenzhongjin.simplifynet.R;
import cn.chenzhongjin.simplifynet.datatrasfer.NetRequest;
import cn.chenzhongjin.simplifynet.datatrasfer.IDataCallBack;
import cn.chenzhongjin.simplifynet.entity.Weather;
import cn.chenzhongjin.simplifynet.ui.activity.view.IWeatherView;
import cn.chenzhongjin.simplifynet.util.LogUtil;

/**
 * @author chenzj
 * @Title: WeatherPresenterCompl
 * @Description: 天气presenter实现
 * @date
 * @email admin@chenzhongjin.cn
 */
public class WeatherPresenterCompl implements IWeatherPresenter {

    Weather weather;
    IWeatherView iWeatherView;
    Handler handler;


    public WeatherPresenterCompl(IWeatherView iWeatherView) {
        this.iWeatherView = iWeatherView;
        handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void clear() {
        iWeatherView.onClearWeatherMes();
    }

    @Override
    public void doSearchWeather(Context context, String cityName) {
        if (null != cityName && cityName.length() > 0) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("cityname", cityName);
            NetRequest.getWeatherMsg(map, new IDataCallBack<Weather>() {
                @Override
                public void onSuccess(Weather weather) {
                    iWeatherView.onSearchWeatherResult(weather);
                }

                @Override
                public void onError(int code, String errorMsg) {
                    LogUtil.i("SearchWeather error mes>" + errorMsg);
                    iWeatherView.onSearchWeatherError(code, errorMsg);
                }
            });
        } else {
            iWeatherView.onInputError(context.getString(R.string.input_error_tips));
        }
    }
}

