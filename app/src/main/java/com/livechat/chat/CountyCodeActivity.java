package com.livechat.chat;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import com.livechat.chat.adapter.CityAdapterS;
import com.livechat.chat.entity.CityEntity;
import com.livechat.chat.entity.CountryBean;
import com.livechat.chat.utils.CommonUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.yokeyword.indexablelistview.IndexEntity;
import me.yokeyword.indexablelistview.IndexHeaderEntity;
import me.yokeyword.indexablelistview.IndexableStickyListView;

/**
 * 国家
 */
public class CountyCodeActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private List<CountryBean> countryList;
    private CityAdapterS cityAdapterS;
    private List<CityEntity> mList = new ArrayList<>();
    private IndexableStickyListView listView;
    private EditText etSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_county_code);

        listView = (IndexableStickyListView) findViewById(R.id.listView);
        etSearch = (EditText) findViewById(R.id.etSearch);
        cityAdapterS = new CityAdapterS(this);
        listView.setAdapter(cityAdapterS);
        etSearch.addTextChangedListener(watcher);
        countryList = new ArrayList<>();
        try {
            // 获取国家和Code
            JSONArray arr = new JSONArray(CommonUtil.getAssetsResourceList(this, "countycode.txt"));
            int size = arr.length();
            for (int i = 0; i < size; i++) {
                CityEntity cityEntity = new CityEntity();
                JSONObject obj = arr.getJSONObject(i);
                cityEntity.setName(obj.getString("country"));
                cityEntity.setCode(obj.getString("code"));
                mList.add(cityEntity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        IndexHeaderEntity<CityEntity> hotHeader = new IndexHeaderEntity<>();
        List<CityEntity> hotEntity = new ArrayList<>();
        hotHeader.setHeaderTitle("热门城市");
        hotHeader.setIndex("热");
        hotHeader.setHeaderList(hotEntity);

        // 绑定数据
        listView.bindDatas(mList, hotHeader);
        listView.setOnItemContentClickListener(new IndexableStickyListView.OnItemContentClickListener() {

            @Override
            public void onItemClick(View v, IndexEntity indexEntity) {
                CityEntity cityEntity = (CityEntity) indexEntity;
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("Country", cityEntity.getName());
                bundle.putString("Code", cityEntity.getCode());
                intent.putExtra("CountryBean", bundle);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private TextWatcher watcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            listView.searchTextChange(s.toString());
        }
    };

    /**
     * 单击ListView的每一项事件
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CountryBean bean = countryList.get(position);
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("Country", bean.getCountry());
        bundle.putString("Code", bean.getCode());
        intent.putExtra("CountryBean", bundle);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_tab_pay_close, R.anim.activity_tab_pay_close);
    }

}
