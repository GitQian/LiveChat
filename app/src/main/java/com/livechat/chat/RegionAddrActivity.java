package com.livechat.chat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Xml;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.livechat.chat.adapter.LocListAdapter;
import com.livechat.chat.entity.CountriesBean;
import com.livechat.chat.service.LocationService;
import com.livechat.chat.service.UserLoginInfoService;
import com.livechat.chat.utils.CommonUtil;
import com.livechat.chat.utils.Constant;
import com.livechat.chat.widget.LoadingDialog;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 地区
 * Created by Administrator on 2016/5/7.
 */
public class RegionAddrActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private TextView tvLocationLocation;
    private ListView lvCountryList;

    private LocListAdapter locListAdapter;
    private LocationService locationService;
    private String sCountry = "", sProvice = "", sCity = "", mCountryStr = "", mProvinceStr = "", mCityStr = "";
    private LoadingDialog loadingDialog;
    private UserLoginInfoService userLoginInfoService;
    private List<CountriesBean> countryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_region_addr);

        initUI();
        initLocData();
    }

    /**
     * 初始化控件UI
     */
    private void initUI() {
        tvLocationLocation = (TextView) this.findViewById(R.id.tvLocationLocation);
        lvCountryList = (ListView) this.findViewById(R.id.lvCountryList);

        Intent intent = this.getIntent();
        mCountryStr = intent.getStringExtra("mCountryStr");
        mProvinceStr = intent.getStringExtra("mProvinceStr");
        mCityStr = intent.getStringExtra("mCityStr");

        userLoginInfoService = new UserLoginInfoService(this);
        countryList = new ArrayList<>();
        locListAdapter = new LocListAdapter(this);
        lvCountryList.setOnItemClickListener(this);
    }

    /**
     * 初始化数据
     */
    private void initLocData() {
        try {
            XmlPullParser pullParser = Xml.newPullParser();// 得到XML解析器
            InputStream is = this.getResources().getAssets().open("CountryList.xml");
            pullParser.setInput(is, "UTF-8");// String inputEncoding格式
            int eventType = pullParser.getEventType();// 得到事件类型
            while (eventType != XmlPullParser.END_DOCUMENT) {// 文档的末尾
                if (pullParser.getEventType() == XmlPullParser.START_TAG) { // 遇到开始标签
                    // 遍历内部的内容
                    String sRootName = pullParser.getName();
                    if ("CountryRegion".equals(sRootName)) {
                        CountriesBean countriesBean = new CountriesBean();
                        countriesBean.setsCode(pullParser.getAttributeValue(null, "Code"));
                        countriesBean.setsName(pullParser.getAttributeValue(null, "Name"));
                        countryList.add(countriesBean);
                    }
                }
                eventType = pullParser.next();// 读取下一个标签
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        locListAdapter.selectCountry(mCountryStr);
        locListAdapter.addCountry(countryList);
        lvCountryList.setAdapter(locListAdapter);
        locListAdapter.notifyDataSetChanged();
    }

    /**
     * 我当前位置
     *
     * @param view
     */
    public void doRlMyLocation(View view) {
        if ((sCountry == null && sProvice == null && sCity == null) || (sCountry.equals("") && sProvice.equals("") && sCity.equals(""))) {
            Toast.makeText(RegionAddrActivity.this, "正在定位中...", Toast.LENGTH_SHORT).show();
            return;
        }
        doUpdateAddr(sCountry, sProvice, sCity);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String country = countryList.get(position).getsName();
        if (country.equals("中国")) {
            Intent intent = new Intent(this, ProvinceListActivity.class);
            intent.putExtra("mProvinceStr", mProvinceStr);
            intent.putExtra("mCityStr", mCityStr);
            startActivity(intent);
        } else {// 其他就不显示了
            doUpdateAddr(country, "", "");
        }
    }

    /**
     * 修改地址
     *
     * @param Countrys  国家
     * @param Provinces 省份
     * @param Citys     城市
     */
    private void doUpdateAddr(final String Countrys, final String Provinces, final String Citys) {
        loadingDialog = new LoadingDialog(this, Constant.THEME_HOLO_LIGHT, "正在修改，请稍后...");
        loadingDialog.show();
        if (!CommonUtil.isNetworkAvailable(this)) {
            CommonUtil.showTips(this, R.mipmap.warning, "网络不可用");
            if (loadingDialog != null) {
                loadingDialog.dismiss();
            }
            return;
        }
        AsyncHttpClient httpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        final String account = App.getInstance().getsUserLoginId();
        params.put("account", account);
        params.put("sign", CommonUtil.md5Encryption(account + Constant.Token));// 签名(md5(account + token))
        final String sAddress = Countrys + " " + Provinces + " " + Citys;
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
                            finish();
                            CommonUtil.showTips(RegionAddrActivity.this, R.mipmap.smile, msg);
                        } else {
                            CommonUtil.showTips(RegionAddrActivity.this, R.mipmap.warning, msg);
                        }
                    } else {
                        CommonUtil.showTips(RegionAddrActivity.this, R.mipmap.warning, "请求失败");
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
                CommonUtil.showTips(RegionAddrActivity.this, R.mipmap.warning, "请求失败");
            }
        });
    }

    /**
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     */
    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (bdLocation == null) {
                return;
            }
            if (bdLocation != null && bdLocation.getLocType() != BDLocation.TypeServerError) {
                String sResult = bdLocation.getCountry() + " " + bdLocation.getProvince() + " " + bdLocation.getCity();
                // 国别
                String country = bdLocation.getCountry();
                if (country != null && !country.equals("")) {
                    sCountry = country;
                } else {
                    sCountry = "";
                }
                // 省份
                String prov = bdLocation.getProvince();
                if (prov != null && !prov.equals("")) {
                    sProvice = prov;//.replace("省", "");
                } else {
                    sProvice = "";
                }
                // 城市
                String city = bdLocation.getCity();
                if (city != null && !city.equals("")) {
                    sCity = city;//.replace("市", "");
                } else {
                    sCity = "";
                }

                try {
                    if (sResult != null && !sResult.equals("")) {
                        tvLocationLocation.setText(sResult);
                    } else {
                        tvLocationLocation.setText("正在定位中...");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        if (!CommonUtil.isNetworkAvailable(this)) {
            CommonUtil.showTips(this, R.mipmap.warning, "网络不可用");
            return;
        }
        // 定位的配置
        locationService = ((App) getApplication()).locationService;
        // 获取locationService实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(mListener);
        // 注册监听
        int type = getIntent().getIntExtra("from", 0);
        if (type == 0) {
            locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        } else if (type == 1) {
            locationService.setLocationOption(locationService.getOption());
        }
        // 定位SDK, start之后会默认发起一次定位请求，开发者无须判断is start并主动调用request
        locationService.start();
    }

    /**
     * Stop location service
     */
    @Override
    protected void onStop() {
        if (locationService != null) {
            locationService.unregisterListener(mListener); //注销掉监听
            locationService.stop(); //停止定位服务
        }
        super.onStop();
    }

}
