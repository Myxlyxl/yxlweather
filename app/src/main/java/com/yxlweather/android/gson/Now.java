package com.yxlweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hasee on 2019/5/1.
 */

public class Now {
    @SerializedName("tmp")
    public String temperature;
    @SerializedName("cond")
    public More more;
    public class More{
        @SerializedName("txt")
        public String info;
    }
}
