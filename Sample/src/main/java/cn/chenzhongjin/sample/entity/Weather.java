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

package cn.chenzhongjin.sample.entity;

import java.io.Serializable;

import cn.chenzhongjin.sample.lib.datatrasfer.DreamLinerResponse;


/**
 * @author chenzj
 * @Title: Weather
 * @Description: 天气实体类
 * @date 2015/11/7
 * @email admin@chenzhongjin.cn
 */
public class Weather extends DreamLinerResponse implements Serializable {

    /**
     * errNum : 0
     * errMsg : success
     * retData : {"city":"广州","pinyin":"guangzhou","citycode":"101280101","date":"15-11-02","time":"11:00","postCode":"510000",
     * "longitude":113.265,"latitude":23.108,"altitude":"43","weather":"多云","temp":"24","l_tmp":"17","h_tmp":"24","WD":"无持续风向",
     * "WS":"微风(<10m/h)","sunrise":"06:32","sunset":"17:48"}
     */

    private int errNum;
    private String errMsg;
    /**
     * city : 广州
     * pinyin : guangzhou
     * citycode : 101280101
     * date : 15-11-02
     * time : 11:00
     * postCode : 510000
     * longitude : 113.265
     * latitude : 23.108
     * altitude : 43
     * weather : 多云
     * temp : 24
     * l_tmp : 17
     * h_tmp : 24
     * WD : 无持续风向
     * WS : 微风(<10m/h)
     * sunrise : 06:32
     * sunset : 17:48
     */

    private RetDataEntity retData;

    public void setErrNum(int errNum) {
        this.errNum = errNum;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public void setRetData(RetDataEntity retData) {
        this.retData = retData;
    }

    public int getErrNum() {
        return errNum;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public RetDataEntity getRetData() {
        return retData;
    }

    public static class RetDataEntity {
        private String city;
        private String pinyin;
        private String citycode;
        private String date;
        private String time;
        private String postCode;
        private double longitude;
        private double latitude;
        private String altitude;
        private String weather;
        private String temp;
        private String l_tmp;
        private String h_tmp;
        private String WD;
        private String WS;
        private String sunrise;
        private String sunset;

        public void setCity(String city) {
            this.city = city;
        }

        public void setPinyin(String pinyin) {
            this.pinyin = pinyin;
        }

        public void setCitycode(String citycode) {
            this.citycode = citycode;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public void setPostCode(String postCode) {
            this.postCode = postCode;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public void setAltitude(String altitude) {
            this.altitude = altitude;
        }

        public void setWeather(String weather) {
            this.weather = weather;
        }

        public void setTemp(String temp) {
            this.temp = temp;
        }

        public void setL_tmp(String l_tmp) {
            this.l_tmp = l_tmp;
        }

        public void setH_tmp(String h_tmp) {
            this.h_tmp = h_tmp;
        }

        public void setWD(String WD) {
            this.WD = WD;
        }

        public void setWS(String WS) {
            this.WS = WS;
        }

        public void setSunrise(String sunrise) {
            this.sunrise = sunrise;
        }

        public void setSunset(String sunset) {
            this.sunset = sunset;
        }

        public String getCity() {
            return city;
        }

        public String getPinyin() {
            return pinyin;
        }

        public String getCitycode() {
            return citycode;
        }

        public String getDate() {
            return date;
        }

        public String getTime() {
            return time;
        }

        public String getPostCode() {
            return postCode;
        }

        public double getLongitude() {
            return longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public String getAltitude() {
            return altitude;
        }

        public String getWeather() {
            return weather;
        }

        public String getTemp() {
            return temp;
        }

        public String getL_tmp() {
            return l_tmp;
        }

        public String getH_tmp() {
            return h_tmp;
        }

        public String getWD() {
            return WD;
        }

        public String getWS() {
            return WS;
        }

        public String getSunrise() {
            return sunrise;
        }

        public String getSunset() {
            return sunset;
        }

        @Override
        public String toString() {
            return "RetDataEntity{" +
                    "altitude='" + altitude + '\'' +
                    ", city='" + city + '\'' +
                    ", pinyin='" + pinyin + '\'' +
                    ", citycode='" + citycode + '\'' +
                    ", date='" + date + '\'' +
                    ", time='" + time + '\'' +
                    ", postCode='" + postCode + '\'' +
                    ", longitude=" + longitude +
                    ", latitude=" + latitude +
                    ", weather='" + weather + '\'' +
                    ", temp='" + temp + '\'' +
                    ", l_tmp='" + l_tmp + '\'' +
                    ", h_tmp='" + h_tmp + '\'' +
                    ", WD='" + WD + '\'' +
                    ", WS='" + WS + '\'' +
                    ", sunrise='" + sunrise + '\'' +
                    ", sunset='" + sunset + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Weather{" +
                "errNum=" + errNum +
                ", errMsg='" + errMsg + '\'' +
                ", retData=" + retData +
                '}';
    }
}

