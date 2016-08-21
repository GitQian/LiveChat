package com.livechat.chat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Xml;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.livechat.chat.adapter.CityAdapter;
import com.livechat.chat.entity.CountriesBean;
import com.livechat.chat.service.UserLoginInfoService;
import com.livechat.chat.utils.CommonUtil;
import com.livechat.chat.utils.Constant;
import com.livechat.chat.widget.LoadingDialog;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 城市
 */
public class CityListActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private ListView lvCity;

    private CityAdapter cityAdapter;
    private List<CountriesBean> provinceList, cityList;
    private LoadingDialog loadingDialog;
    private UserLoginInfoService userLoginInfoService;
    private String sProvince = "", mProvinceStr = "", mCityStr = "";
    private int provinceCode;
    private int subscript=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list);

        initUI();
        initCityData();
    }

    /**
     * 初始化控件UI
     */
    private void initUI() {
        lvCity = (ListView) this.findViewById(R.id.lvCity);

        Intent intent = this.getIntent();
        provinceCode = intent.getIntExtra("provinceCode", 0);
        sProvince = intent.getStringExtra("province");
        mCityStr = intent.getStringExtra("mCityStr");
        if(sProvince.equals("北京")||sProvince.equals("上海")){
            postLocation(sProvince);
        }
        userLoginInfoService = new UserLoginInfoService(this);
        cityList = new ArrayList<>();
        provinceList = new ArrayList<>();
        cityAdapter = new CityAdapter(this);
        lvCity.setOnItemClickListener(this);
    }

    /**
     * 初始化城市数据
     */
    private void initCityData() {
        try {
            mProvinceStr = CommonUtil.getAssetsResourceList(this, "ProvinceCityList.txt");
            JSONArray jsonArray = new JSONArray(mProvinceStr);
            JSONObject object = jsonArray.getJSONObject(provinceCode);
            JSONArray cityArray = object.getJSONArray("City");
            for (int i = 0; i < cityArray.length(); i++) {
                CountriesBean countriesBean = new CountriesBean();
                JSONObject jsonObject = cityArray.getJSONObject(i);
                countriesBean.setsName(jsonObject.getString("Name"));
                countriesBean.setsCode(jsonObject.getString("Code"));
                cityList.add(countriesBean);
            }
            cityAdapter.selected(mCityStr);
            cityAdapter.addCity(cityList);
            lvCity.setAdapter(cityAdapter);
            cityAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化城市数据
     */
    private void initCityData1() {
        try {
            XmlPullParser pullParser = Xml.newPullParser();// 得到XML解析器
            InputStream is = this.getResources().getAssets().open("ProvinceCityList.xml");
            pullParser.setInput(is, "UTF-8");
            int eventType = pullParser.getEventType();// 得到事件类型
            while (eventType != XmlPullParser.END_DOCUMENT) {// 文档的末尾
                // 当前节点的名称
                String nodeName = pullParser.getName();
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:// 文档开始事件,可以进行数据初始化处理
                        break;
                    case XmlPullParser.START_TAG:// 开始元素事件
                        if ("State".equals(nodeName)) {
                            CountriesBean countriesBean1 = new CountriesBean();
                            countriesBean1.setsCode(pullParser.getAttributeValue(0));
                            countriesBean1.setsName(pullParser.getAttributeValue(1));
                            provinceList.add(countriesBean1);
                        } else if ("City".equals(nodeName)) {
                            CountriesBean countriesBean2 = new CountriesBean();
                            countriesBean2.setsCode(pullParser.getAttributeValue(0));
                            countriesBean2.setsName(pullParser.getAttributeValue(1));
                            cityList.add(countriesBean2);
                        }
                        break;
                    case XmlPullParser.END_TAG:// 结束元素事件
                        break;
                }
                eventType = pullParser.next();// 读取下一个标签
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        cityAdapter.addCity(cityList);
        lvCity.setAdapter(cityAdapter);
        cityAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        loadingDialog = new LoadingDialog(this, Constant.THEME_HOLO_LIGHT, "正在操作，请稍后...");
        loadingDialog.show();
        if (!CommonUtil.isNetworkAvailable(this)) {
            CommonUtil.showTips(this, R.mipmap.warning, "网络不可用");
            if (loadingDialog != null) {
                loadingDialog.dismiss();
            }
            return;
        }
        postLocation(cityList.get(position).getsName());
    }

    /**
     * 提交位置
     */
    public void postLocation(String City){

        AsyncHttpClient httpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        final String account = App.getInstance().getsUserLoginId();
        params.put("account", account);
        params.put("sign", CommonUtil.md5Encryption(account + Constant.Token));// 签名(md5(account + token))
        final String sAddress = "中国 " + sProvince + " " + City;
        params.put("address", sAddress);
        httpClient.post(Constant.sUpdateUserInfoUrl, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    if (loadingDialog != null) {
                        loadingDialog.dismiss();
                    }

                    String sresponse = String.valueOf(response);
                    com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(sresponse);
                    String msg = jsonObject.getString("message");
                    int code = jsonObject.getInteger("code");

                    if (statusCode == 200) {
                        if (code == 0) {
                            // 修改本地地址数据
                            userLoginInfoService.updateUsersAddresses(sAddress, account);
                            Intent intent = new Intent(CityListActivity.this, PersonInfoActivity.class);
                            startActivity(intent);
                            CommonUtil.showTips(CityListActivity.this, R.mipmap.smile, msg);
                        } else {
                            CommonUtil.showTips(CityListActivity.this, R.mipmap.warning, msg);
                        }
                    } else {
                        CommonUtil.showTips(CityListActivity.this, R.mipmap.warning, "请求失败");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                if (loadingDialog != null) {
                    loadingDialog.dismiss();
                }
                CommonUtil.showTips(CityListActivity.this, R.mipmap.warning, "请求失败");
            }
        });
    }

}
