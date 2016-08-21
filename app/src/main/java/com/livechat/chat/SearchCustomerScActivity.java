package com.livechat.chat;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.livechat.chat.adapter.SearchCustomerScAdapter;
import com.livechat.chat.entity.CustomerBean;
import com.livechat.chat.service.CustomerInfoService;

import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;

/**
 * 搜索客服
 */
public class SearchCustomerScActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private EditText etSearchCustomerSc;
    private ListView mLvCustomerSc;

    private String mContent = "";
    private List<CustomerBean> customerBeanList;
    private CustomerInfoService customerInfoService;
    private SearchCustomerScAdapter searchCustomerScAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_customer_sc);

        customerInfoService = new CustomerInfoService(this);
        searchCustomerScAdapter = new SearchCustomerScAdapter(this);

        etSearchCustomerSc = (EditText) this.findViewById(R.id.etSearchCustomerSc);
        mLvCustomerSc = (ListView) this.findViewById(R.id.mLvCustomerSc);

        etSearchCustomerSc.addTextChangedListener(watcher);
        mLvCustomerSc.setOnItemClickListener(this);
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
            mContent = s.toString();
            if (TextUtils.isEmpty(mContent)) {
                if (customerBeanList != null && customerBeanList.size() > 0) {
                    customerBeanList.clear();
                }
                return;
            }
            loadChat(mContent);
        }
    };

    /**
     * 更新Adapter
     *
     * @param name
     */
    public void loadChat(String name) {
        // 查询本地数据显示
        customerBeanList = customerInfoService.getListCustomerInfo(name);
        if (customerBeanList != null && customerBeanList.size() > 0) {
            searchCustomerScAdapter.addSearchCustomerSc(customerBeanList);
            mLvCustomerSc.setAdapter(searchCustomerScAdapter);
            searchCustomerScAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 进入聊天
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (RongIM.getInstance() != null) {
            // 上下文, 类型, 目标ID, 内容(对方昵称)
            RongIM.getInstance().startConversation(this, Conversation.ConversationType.PRIVATE, customerBeanList.get(position).getsAccount(), customerBeanList.get(position).getsNickname());
        }
    }

}
