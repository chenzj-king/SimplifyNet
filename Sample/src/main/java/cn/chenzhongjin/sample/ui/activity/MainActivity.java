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

package cn.chenzhongjin.sample.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.chenzhongjin.sample.R;
import cn.chenzhongjin.sample.entity.Weather;
import cn.chenzhongjin.sample.ui.activity.presenter.WeatherPresenterCompl;
import cn.chenzhongjin.sample.ui.activity.view.IWeatherView;

public class MainActivity extends AppCompatActivity implements IWeatherView {

    @BindView(R.id.cityname_editText)
    AppCompatEditText cityNameEditText;
    @BindView(R.id.search_weather_button)
    AppCompatButton searchButton;
    @BindView(R.id.weather_mes_textView)
    AppCompatTextView weatherMesTextView;

    WeatherPresenterCompl weatherPresenterCompl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        weatherPresenterCompl = new WeatherPresenterCompl(this);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.search_weather_button)
    void clickSearchButton() {
        weatherMesTextView.setText(R.string.search_weather_tips);
        weatherPresenterCompl.doSearchWeather(getApplicationContext(), cityNameEditText.getText().toString());
    }

    @Override
    public void onInputError(String errorMsg) {
        cityNameEditText.setError(errorMsg);
    }

    @Override
    public void onClearWeatherMes() {
        cityNameEditText.setText("");
        weatherMesTextView.setText(R.string.before_search_weather_tips);
    }

    @Override
    public void onSearchWeatherResult(Weather weather) {
        if (null != weather) {
            weatherMesTextView.setText(weather.getRetData().toString());
        }
    }

    @Override
    public void onSearchWeatherError(int code, String errorMsg) {
        if (null != errorMsg && errorMsg.length() > 0) {
            weatherMesTextView.setText(errorMsg);
        }
    }
}
