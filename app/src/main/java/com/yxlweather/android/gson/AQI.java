package com.yxlweather.android.gson;

/**
 * Created by hasee on 2019/5/1.
 */

public class AQI {
    public AQICity city;
    public class AQICity {
        public String aqi;
        public String pm25;
    }
}
