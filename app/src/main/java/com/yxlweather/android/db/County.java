package com.yxlweather.android.db;

import org.litepal.crud.DataSupport;
/**
 * Created by hasee on 2019/4/24.
 */

public class County extends DataSupport {
    private int id;
    private String countyName;
    private String weatherId; // 县所对应的天气
    private int cityId; // 县所属的市的 id 值
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getCountyName() {
        return countyName;
    }
    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }
    public String getWeatherId() {
        return weatherId;
    }
    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }
    public int getCityId() {
        return cityId;
    }
    public void setCityId (int cityId) {
        this.cityId = cityId;
    }
}
