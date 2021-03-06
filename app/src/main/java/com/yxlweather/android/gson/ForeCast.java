package com.yxlweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hasee on 2019/5/1.
 */

public class ForeCast {
    public String date;
    @SerializedName("cond")
    public More more;
    public class More {
        @SerializedName("txt_d")
        public String info;
    }
    @SerializedName("tmp")
    public Temperature temperature;
    public class Temperature {
        public String max;
        public String min;
    }
}
