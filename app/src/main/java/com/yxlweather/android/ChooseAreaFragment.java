package com.yxlweather.android;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yxlweather.android.db.City;
import com.yxlweather.android.db.County;
import com.yxlweather.android.db.Province;
import com.yxlweather.android.util.HttpUtil;
import com.yxlweather.android.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.lang.Runnable;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by hasee on 2019/4/26.
 */

public class ChooseAreaFragment extends Fragment {
    private final String TAG = "ChooseAreaFragment";
    private final int LEVEL_PROVINCE = 0;
    private final int LEVEL_CITY = 1;
    private final int LEVEL_COUNTY = 2;
    private ProgressDialog progressDialog;
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
    private List<County> countyList;
    // 被选中的省
    private Province chosenProvince;
    // 被选中的市
    private City chosenCity;
    // 被选中的县
    private County chosenCountry;
    // 当前被选中的级别
    private int currentLevel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_area, container, false);
        titleText = (TextView) view.findViewById(R.id.title_text);
        backButton = (Button) view.findViewById(R.id.back_button);
        listView = (ListView) view.findViewById(R.id.list_view);
        // 初始化 adapter ，将它设置为 listView 的适配器
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(adapter);
        return view;
    }

    @Override
    // 给 listView 和 backButton 设置点击事件
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentLevel == LEVEL_PROVINCE) {
                    chosenProvince = provinceList.get(position);
                    queryCities();
                }
                else if (currentLevel == LEVEL_CITY) {
                    chosenCity = cityList.get(position);
                    queryCounties();
                }
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentLevel == LEVEL_COUNTY) {
                    queryCities();
                }
                else if (currentLevel == LEVEL_CITY) {
                    queryProvinces();
                }
            }
        });
        queryProvinces();
    }

    /**
     *  查询所有的省
     *  优先从数据库搜索
     *  如果数据库中没有就到服务器查找
     */
    public void queryProvinces() {
        titleText.setText("中国");
        backButton.setVisibility(View.GONE);
        provinceList = DataSupport.findAll(Province.class);
        if (provinceList.size() > 0) {
            dataList.clear();
            for (Province province : provinceList) {
                dataList.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_PROVINCE;
        }
        else {
            String address = "http://guolin.tech/api/china";
            queryFromServer(address, "province");
        }
    }

    /**
     * 查询所有的市
     */
    public void queryCities() {
        titleText.setText(chosenProvince.getProvinceName());
        backButton.setVisibility(View.VISIBLE);
        cityList = DataSupport.where("provinceid = ?", String.valueOf(chosenProvince.getId())).find(City.class);
        if (cityList.size() > 0) {
            dataList.clear();
            for (City city : cityList) {
                dataList.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_CITY;
        }
        else {
            int provinceCode = chosenProvince.getProvinceCode();
            String address = "http://guolin.tech/api/china/" + provinceCode;
            queryFromServer(address, "city");
        }
    }

    /**
     *  查询所有的县
     */
    private void queryCounties() {
        titleText.setText(chosenCity.getCityName());
        backButton.setVisibility(View.VISIBLE);
        countyList = DataSupport.where("cityid = ?", String.valueOf(chosenCity.getId())).find(County.class);
        if (countyList.size() > 0) {
            dataList.clear();
            for (County county : countyList) {
                dataList.add(county.getCountyName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_COUNTY;
        } else {
            int provinceCode = chosenProvince.getProvinceCode();
            int cityCode = chosenCity.getCityCode();
            String address = "http://guolin.tech/api/china/" + provinceCode + "/" + cityCode;
            queryFromServer(address, "county");
        }
    }

    /**
     * @param address
     * @param type
     *  输入地址和要求的类型
     *  查询市县数据
     */
    public void queryFromServer(String address, final String type) {
        showProgressDialog();
        HttpUtil.sendOkHttpRequest(address, new Callback() {


            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string(); // 保存返回信息
                boolean result = false;
                if ("province".equals(type)) {
                    result = Utility.handleProvinceResponse(responseText);
                }
                else if ("city".equals(type)) {
                    result = Utility.handleCiityResponse(responseText, chosenProvince.getId());
                }
                else if ("county".equals(type)) {
                    result = Utility.handleCountyResponse(responseText, chosenCity.getId());
                }
                if (result) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if ("province".equals(type)) {
                                queryProvinces();
                            }
                            else if ("city".equals(type)) {
                                queryCities();
                            }
                            else if ("county".equals(type)) {
                                queryCounties();
                            }
                        }
                    });
                }
            }
            @Override
            public void onFailure(Call call, IOException e) {
                // 回到主线程处理逻辑
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(getContext(), "加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    // 显示进度条
    public void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("加载中...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }
    // 关闭进度条
    public void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}

