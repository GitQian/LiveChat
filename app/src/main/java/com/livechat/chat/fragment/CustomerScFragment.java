package com.livechat.chat.fragment;

import android.content.Intent;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.andview.refreshview.XRefreshView;
import com.livechat.chat.App;
import com.livechat.chat.R;
import com.livechat.chat.SearchCustomerScActivity;
import com.livechat.chat.adapter.ConversationListAdapter;
import com.livechat.chat.entity.CustomerBean;
import com.livechat.chat.entity.Friend;
import com.livechat.chat.entity.UserLoginInfoBean;
import com.livechat.chat.service.CustomerInfoService;
import com.livechat.chat.service.UserLoginInfoService;
import com.livechat.chat.utils.CommonUtil;
import com.livechat.chat.utils.Constant;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;

/**
 * 客服中心
 * Created by Administrator on 2016/3/21.
 */
public class CustomerScFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener, RongIM.OnReceiveUnreadCountChangedListener {

    private ImageButton ivBack;
    private ListView lvChatList;
    private TextView tvSearch, tvNull;

    private List<Friend> friendList;
    private List<CustomerBean> customerBeanList;
    private CustomerInfoService customerInfoService;
    private UserLoginInfoService userLoginInfoService;
    private ConversationListAdapter conversationListAdapter;
    private RequestQueue mQueue;
    private XRefreshView refreshView;

    private Handler mHandler;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer_sc_layout, container, false);
        mQueue= Volley.newRequestQueue(getActivity());
        ivBack = (ImageButton) view.findViewById(R.id.ivBack);
        lvChatList = (ListView) view.findViewById(R.id.lvChatList);
        tvSearch = (TextView) view.findViewById(R.id.tvSearch);
        tvNull = (TextView) view.findViewById(R.id.tvNull);
        refreshView=(XRefreshView)view.findViewById(R.id.custom_vew);

        conversationListAdapter = new ConversationListAdapter(getContext(),mQueue);
//        conversationListAdapter.registerDataSetObserver(observer);
        customerInfoService = new CustomerInfoService(getContext());
        userLoginInfoService = new UserLoginInfoService(getContext());
        customerBeanList = new ArrayList<>();
        friendList = new ArrayList<>();

        ivBack.setOnClickListener(this);
        lvChatList.setOnItemClickListener(this);
        tvSearch.setOnClickListener(this);

        lvChatList.setAdapter(conversationListAdapter);

        postConversationInfo();
        setUserInfo();

        mHandler = new Handler();
        RongIM.setOnReceiveMessageListener(new RongIMClient.OnReceiveMessageListener() {
            @Override
            public boolean onReceived(Message message, int i) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            loadChat("");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                return false;
            }
        });
        initRongMessage();

        // 设置是否可以下拉刷新
       refreshView.setPullRefreshEnable(true);
        // 设置是否可以上拉加载
        refreshView.setPullLoadEnable(false);
        //设置刷新时间
        refreshView.restoreLastRefreshTime(System.currentTimeMillis());
        // 设置时候可以自动刷新
        refreshView.setAutoRefresh(false);
//        refreshView.addTextChangedListener(watcher);

        refreshView.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {

            @Override
            public void onRefresh() {
                loadChat("");
                new android.os.Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        refreshView.stopRefresh();
                    }
                }, 1000);
//                refreshView.stopRefresh();
//                initData();
            }

            @Override
            public void onLoadMore(boolean isSlience) {

                new android.os.Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
//                        refreshView.stopLoadMore(true);
                    }
                }, 1500);
//                refreshView.stopLoadMore(false);
            }

            @Override
            public void onRelease(float direction) {
                super.onRelease(direction);
                if (direction > 0) {
                    System.out.println("下拉");
                } else {
                    System.out.println("上拉");
                }
            }
        });



        return view;
    }

    @Override
    public void onResume() {
//        // 查询本地数据显示
//        customerBeanList = customerInfoService.getListCustomerInfo();
//        if (customerBeanList != null && customerBeanList.size() > 0) {
//            lvChatList.setVisibility(View.VISIBLE);
//            tvNull.setVisibility(View.GONE);
//        } else {
//            lvChatList.setVisibility(View.GONE);
//            tvNull.setVisibility(View.VISIBLE);
//        }
//        conversationListAdapter.add(customerBeanList);
//        lvChatList.setAdapter(conversationListAdapter);
//        conversationListAdapter.notifyDataSetChanged();
        loadChat("");
        super.onResume();
    }

    /**
     * 融云消息接收，及初始化
     */
    private void initRongMessage() {
        final Conversation.ConversationType[] conversationTypes = {Conversation.ConversationType.PRIVATE, Conversation.ConversationType.DISCUSSION,
                Conversation.ConversationType.GROUP, Conversation.ConversationType.SYSTEM,
                Conversation.ConversationType.PUBLIC_SERVICE, Conversation.ConversationType.APP_PUBLIC_SERVICE};

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                RongIM.getInstance().setOnReceiveUnreadCountChangedListener(CustomerScFragment.this, conversationTypes);
//				RongIM.getInstance().setOnReceiveUnreadCountChangedListener(mCountListener1, Conversation.ConversationType.APP_PUBLIC_SERVICE);
            }
        }, 500);


    }

    /**
     * 更新Adapter
     * @param name
     */
    public void loadChat(String name){
        // 查询本地数据显示
        if (customerBeanList.size() != 0) {
            customerBeanList.clear();
        }
        customerBeanList = customerInfoService.getListCustomerInfo(name);
        if (customerBeanList != null && customerBeanList.size() > 0) {
            lvChatList.setVisibility(View.VISIBLE);
            tvNull.setVisibility(View.GONE);
        } else {
            lvChatList.setVisibility(View.GONE);
            tvNull.setVisibility(View.VISIBLE);
        }
        for(int i=0;i<customerBeanList.size();i++){
            List<Message> msgList = RongIM.getInstance().getRongIMClient().getLatestMessages(Conversation.ConversationType.PRIVATE, customerBeanList.get(i).getsAccount(), 1);
            if (null!=msgList){
                if(msgList.size()>0){
                    customerBeanList.get(i).setSentTime(msgList.get(msgList.size()-1).getSentTime());
                    customerBeanList.get(i).setContent(msgList.get(msgList.size()-1).getContent());
                }
            }
        }
        for(int n=0;n<customerBeanList.size()-1;n++){
            CustomerBean CB;
            for(int m=n;m<customerBeanList.size();m++){
                if(customerBeanList.get(n).getSentTime()==0){
                    CB=customerBeanList.get(n);
                    customerBeanList.set(n,customerBeanList.get(m));
                    customerBeanList.set(m,CB);
                }else if(customerBeanList.get(m).getSentTime()>customerBeanList.get(n).getSentTime()){
                    CB=customerBeanList.get(n);
                    customerBeanList.set(n,customerBeanList.get(m));
                    customerBeanList.set(m,CB);
                }
            }
        }

        conversationListAdapter.add(customerBeanList);
        conversationListAdapter.notifyDataSetChanged();
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                lvChatList.invalidateViews();
                lvChatList.invalidate();
            }
        });

    }

    /**
     * 一次获取(请求)客服的数据
     */
    private void postConversationInfo() {
        if (!CommonUtil.isNetworkAvailable(getActivity())) {
            CommonUtil.showTips(getActivity(), R.mipmap.warning, "网络不可用");
            return;
        }
        AsyncHttpClient httpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        String account = App.getInstance().getsUserLoginId();
        params.put("account", account);// 用户账号
        params.put("sign", CommonUtil.md5Encryption("0" + Constant.Token));
        httpClient.post(Constant.sFetchCustomersUrl, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    String sresponse = String.valueOf(response);
                    com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(sresponse);
                    String msg = jsonObject.getString("message");
                    int code = jsonObject.getInteger("code");
                    JSONArray dataArray = jsonObject.getJSONArray("data");
                    if (statusCode == 200) {
                        if (code == 0) {
                            if (dataArray != null && dataArray.size() > 0) {
                                for (int i = 0; i < dataArray.size(); i++) {
                                    com.alibaba.fastjson.JSONObject object = (com.alibaba.fastjson.JSONObject) dataArray.get(i);
                                    String sAccount = object.getString("account") == null ? "" : object.getString("account");
                                    String sHeaderImage = object.getString("headerImage") == null ? "" : object.getString("headerImage");
                                    String sRongToken = object.getString("rongToken") == null ? "" : object.getString("rongToken");
                                    String sNickname = object.getString("nickname") == null ? "" : object.getString("nickname");
                                    int iOnlineStatus = object.getInteger("onlineStatus") == null ? 0 : object.getInteger("onlineStatus");
                                    CustomerBean customerBean = new CustomerBean();
                                    customerBean.setsAccount(sAccount);
                                    customerBean.setsHeaderImage(sHeaderImage);
                                    customerBean.setsRongToken(sRongToken);
                                    customerBean.setsNickname(sNickname);
                                    customerBean.setiOnlineStatus(iOnlineStatus);
                                    // 添加客服数据或者更新客服数据
                                    if (customerInfoService.isExists(sAccount) > 0) {// 存在就更新
                                        customerInfoService.updateCustomerInfo(customerBean);
                                    } else {// 不存在就添加
                                        customerInfoService.insertCustomerInfo(customerBean);
                                    }
                                }
                            }
                            loadChat("");
                        } else if (code == 9001) {
                            CommonUtil.showTips(getActivity(), R.mipmap.warning, msg);
                        } else {
                            CommonUtil.showTips(getActivity(), R.mipmap.warning, msg);
                        }
                    } else {
                        CommonUtil.showTips(getActivity(), R.mipmap.warning, "请求失败");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                CommonUtil.showTips(getActivity(), R.mipmap.warning, "请求失败");
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (RongIM.getInstance() != null) {
            // 上下文, 类型, 目标ID, 内容(对方昵称)
//            conversationListAdapter.add(customerBeanList);
            conversationListAdapter.notifyDataSetChanged();
            lvChatList.invalidateViews();
            lvChatList.invalidate();
            RongIM.getInstance().startConversation(getContext(), Conversation.ConversationType.PRIVATE, customerBeanList.get(position).getsAccount(), customerBeanList.get(position).getsNickname());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:// 退出
                break;
            case R.id.tvSearch:// 输入框
                Intent intent = new Intent(getContext(), SearchCustomerScActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * 刷新用户信息
     */
    private void setUserInfo() {
        UserLoginInfoBean userLoginInfoBean = userLoginInfoService.getSimpleUserLoginInfo(CommonUtil.getUserLoginInfo(getActivity(), Constant.ACCOUNT_CODE));
        if (userLoginInfoBean != null) {
            friendList.add(new Friend(userLoginInfoBean.getsAccount(), userLoginInfoBean.getsNickname(), userLoginInfoBean.getsHeaderImage()));
            customerBeanList = customerInfoService.getListCustomerInfo();
            if (customerBeanList != null && customerBeanList.size() > 0) {
                for (int i = 0; i < customerBeanList.size(); i++) {
                    CustomerBean customerBean = customerBeanList.get(i);
                    friendList.add(new Friend(customerBean.getsAccount(), customerBean.getsNickname(), customerBean.getsHeaderImage()));
                }
            }
        }

        RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {

            @Override
            public UserInfo getUserInfo(String userId) {
                if (friendList != null && friendList.size() > 0) {
                    for (Friend f : friendList) {
                        if (f.getsUserId().equals(userId)) {
//                            Log.d("LiveChatMainActivity", "--getUserInfo：" + userId);
                            // 根据 userId 去你的用户系统里查询对应的用户信息返回给融云 SDK。
                            return new UserInfo(f.getsUserId(), f.getsUserName(), Uri.parse(f.getsPortraitUri()));
                        }
                    }
                }
//                Log.d("LiveChatMainActivity", "--getUserInfo：" + userId);
                return null;
            }
        }, true);
    }

    private DataSetObserver observer = new DataSetObserver() {

        @Override
        public void onChanged() {
            super.onChanged();
//            if (customerBeanList != null && customerBeanList.size() > 0) {
//                customerBeanList.clear();
//            }
//            customerBeanList = customerInfoService.getListCustomerInfo();
//            if (customerBeanList != null && customerBeanList.size() > 0) {
//                lvChatList.setVisibility(View.VISIBLE);
//                tvNull.setVisibility(View.GONE);
//            } else {
//                lvChatList.setVisibility(View.GONE);
//                tvNull.setVisibility(View.VISIBLE);
//            }
//            conversationListAdapter.add(customerBeanList);
//            lvChatList.setAdapter(conversationListAdapter);
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
        }
    };

    @Override
    public void onMessageIncreased(int i) {
        Log.i("qiansheng", "Count:" + i);
//        mUnreadCount.setVisibility(View.GONE);
    }
}
