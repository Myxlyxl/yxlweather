package com.yxlweather.android.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hasee on 2019/5/1.
 */

public class Weather {
    public String status;
    public Basic basic;
    public AQI aqi;
    public Now now;
    public Suggestion suggestion;
    @SerializedName("daily_weather")
    public List<Weather> forecastList;
}
