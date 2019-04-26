package com.yxlweather.android;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.yxlweather.android.db.City;
import com.yxlweather.android.db.Country;
import com.yxlweather.android.db.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hasee on 2019/4/26.
 */

public class ChooseAreaFragment extends Fragment {
    private final int LEVEL_PROVINCE = 0;
    private final int LEVEL_CITY = 1;
    private final int LEVEL_COUNTRY = 2;
    private TextView titleText;
    private Button backButton;
    private ListView listView;
    private List<String> dataList = new ArrayList<>();
    /**
     * 传递给 ListView 的数据
     * ArrayAdapter 可以通过泛型来指定要适配的数据类型
     */
    private ArrayAdapter<String> adapter;
    // 省列表
    private List<Province> provinceList;
    // 市列表
    private List<City> cityList;
    // 县列表
    private List<Country> countryList;
    // 被选中的省
    private Province chosenProvince;
    // 被选中的市
    private City chosenCity;
    // 被选中的县
    private Country chosenCountry;
    // 当前被选中的级别
    private int currentLevel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_area, container, false);
        titleText = (TextView) view.findViewById(R.id.title_text);
        backButton = (Button) view.findViewById(R.id.back_button);
        listView = (ListView) view.findViewById(R.id.list_view);
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(adapter);
        return view;
    }
}
