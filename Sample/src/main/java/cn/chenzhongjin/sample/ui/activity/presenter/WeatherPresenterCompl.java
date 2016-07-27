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

package cn.chenzhongjin.sample.ui.activity.presenter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import cn.chenzhongjin.sample.R;
import cn.chenzhongjin.sample.entity.Weather;
import cn.chenzhongjin.sample.net.GenericsCallback;
import cn.chenzhongjin.sample.net.NetRequest;
import cn.chenzhongjin.sample.ui.activity.view.IWeatherView;
import okhttp3.Call;

/**
 * @author chenzj
 * @Title: WeatherPresenterCompl
 * @Description: 天气presenter实现
 * @date
 * @email admin@chenzhongjin.cn
 */
public class WeatherPresenterCompl implements IWeatherPresenter {

    private static final String TAG = "WeatherPresenterCompl";

    private UUID mUUID = UUID.randomUUID();

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
        if (!TextUtils.isEmpty(cityName)) {
            Map<String, String> map = new HashMap<>();
            map.put("cityname", cityName);

            NetRequest.getWeatherMsg(map, mUUID, new GenericsCallback<Weather>() {
                @Override
                public void onError(int errorCode, String errorMes, Call call, Exception e) {
                    Logger.t(TAG).i("errorCode=" + errorCode + "\t\t" + errorMes);
                    iWeatherView.onSearchWeatherError(errorCode, errorMes);
                }

                @Override
                public void onResponse(Weather response) {
                    iWeatherView.onSearchWeatherResult(response);
                }
            });
        } else {
            iWeatherView.onInputError(context.getString(R.string.input_error_tips));
        }
    }

}



