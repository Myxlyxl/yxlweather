package com.yxlweather.android.db;

import org.litepal.crud.DataSupport;
/**
 * Created by hasee on 2019/4/24.
 */

public class Province extends DataSupport {
    private int id;
    private String provinceName;
    private int provinceCode;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getProvinceName() {
        return provinceName;
    }
    public void setProvinceName(String pName) {
        this.provinceName = pName;
    }
    public int getProvinceCode() {
        return provinceCode;
    }
    public void setProvinceCode(int pCode) {
        this.provinceCode = pCode;
    }
}
