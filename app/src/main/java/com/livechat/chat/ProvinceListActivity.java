package com.livechat.chat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Xml;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.livechat.chat.adapter.ProvinceAdapter;
import com.livechat.chat.entity.CountriesBean;
import com.livechat.chat.utils.CommonUtil;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 省份
 */
public class ProvinceListActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private ListView lvProvince;

    private ProvinceAdapter provinceAdapter;
    private List<CountriesBean> provinceList;
    private String mProvinceStr = "", mProvinceStr1 = "", mCityStr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provinces_list);

        initUI();
        initProvinceData();
    }

    /**
     * 初始化控件UI
     */
    private void initUI() {
        lvProvince = (ListView) this.findViewById(R.id.lvProvince);

        Intent intent = this.getIntent();
        mProvinceStr1 = intent.getStringExtra("mProvinceStr");
        mCityStr = intent.getStringExtra("mCityStr");

        provinceList = new ArrayList<>();
        provinceAdapter = new ProvinceAdapter(this);
        lvProvince.setOnItemClickListener(this);
    }

    /**
     * 初始化身份数据
     */
    private void initProvinceData() {
        try {
            mProvinceStr = CommonUtil.getAssetsResourceList(this, "ProvinceCityList.txt");
            JSONArray jsonArray = new JSONArray(mProvinceStr);
            for (int i = 0; i < jsonArray.length(); i++) {
                CountriesBean countriesBean = new CountriesBean();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                countriesBean.setsName(jsonObject.getString("Name"));
                countriesBean.setsCode(jsonObject.getString("Code"));
                provinceList.add(countriesBean);
            }
            provinceAdapter.selected(mProvinceStr1);
            provinceAdapter.addProvince(provinceList);
            lvProvince.setAdapter(provinceAdapter);
            provinceAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化身份数据
     */
    private void initProvinceData1() {
        try {
            XmlPullParser pullParser = Xml.newPullParser();// 得到XML解析器
            InputStream is = this.getResources().getAssets().open("ProvinceCityList.xml");
            pullParser.setInput(is, "UTF-8");// String inputEncoding格式
            int eventType = pullParser.getEventType();// 得到事件类型
            while (eventType != XmlPullParser.END_DOCUMENT) {// 文档的末尾
                if (pullParser.getEventType() == XmlPullParser.START_TAG) { // 遇到开始标签
                    // 遍历内部的内容
                    String sRootName = pullParser.getName();

                    if ("State".equals(sRootName)) {
                        CountriesBean countriesBean = new CountriesBean();
                        countriesBean.setsCode(pullParser.getAttributeValue(null, "Code"));
                        countriesBean.setsName(pullParser.getAttributeValue(null, "Name"));
                        provinceList.add(countriesBean);
                    }
                }
                eventType = pullParser.next();// 读取下一个标签
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        provinceAdapter.addProvince(provinceList);
        lvProvince.setAdapter(provinceAdapter);
        provinceAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, CityListActivity.class);
        intent.putExtra("provinceCode", position);
        intent.putExtra("province", provinceList.get(position).getsName());
        intent.putExtra("mCityStr", mCityStr);
        startActivity(intent);
    }

}
