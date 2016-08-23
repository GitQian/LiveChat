package com.livechat.chat.fragment;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.livechat.chat.R;
import com.livechat.chat.adapter.InfoActAdapter;
import com.livechat.chat.adapter.MemberAdapter;
import com.livechat.chat.entity.EotherUrlBean;
import com.livechat.chat.entity.MaterialInfoBean;
import com.livechat.chat.service.MaterialInfoService;
import com.livechat.chat.utils.CommonUtil;
import com.livechat.chat.utils.Constant;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 资讯活动
 * Created by Administrator on 2016/3/21.
 */
public class InfoActFragment extends Fragment implements View.OnClickListener {

    private ListView lvInfoAct;
    private RelativeLayout rlDrop, rlUp;

    private PopupWindow popupWindow = null;
    private InfoActAdapter infoActBaseAdapter;
    private MaterialInfoService materialInfoService;
    private List<MaterialInfoBean> materialInfoBeanList;
    //    private InfoActPullDownAdapter infoActPullDownAdapter;
    private List<EotherUrlBean> eotherUrlBeanList;
    private RequestQueue mQueue;
    //    private List<InformationBean> informationBeans;
//    private InfoActListAdapter infoActListAdapter;
    private int pageNumber = 1;
    private EditText etSearch;
    private String mContent = "";
//    private XRefreshView refreshView;
    View view2;
    private LinearLayout ll_refresh;
    private TextView text_load2,tvUpdateTime;
    private ProgressBar pb;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_infoact_layout, container, false);
        mQueue= Volley.newRequestQueue(getActivity());
        infoActBaseAdapter = new InfoActAdapter(getActivity(),mQueue);
        materialInfoService = new MaterialInfoService(getContext());
//        infoActPullDownAdapter = new InfoActPullDownAdapter(getContext());
        eotherUrlBeanList = new ArrayList<>();
//        informationBeans=new ArrayList<>();
        //infoActListAdapter=new InfoActListAdapter(getActivity());

        lvInfoAct = (ListView) view.findViewById(R.id.lvInfoAct);
        rlDrop = (RelativeLayout) view.findViewById(R.id.rlDrop);
        rlUp = (RelativeLayout) view.findViewById(R.id.rlUp);
        etSearch = (EditText) view.findViewById(R.id.etSearch);
//        refreshView = (XRefreshView) view.findViewById(R.id.custom_vew);
        //lvInfoAct.setAdapter(infoActListAdapter);
        lvInfoAct.setAdapter(infoActBaseAdapter);

//        mInflater== LayoutInflater.from(getActivity());
        view2=LayoutInflater.from(getActivity()).inflate(R.layout.list_bottom,null);
        ll_refresh=(LinearLayout)view2.findViewById(R.id.ll_refresh);
        text_load2=(TextView)view2.findViewById(R.id.text_load2);
        tvUpdateTime=(TextView)view2.findViewById(R.id.tvUpdateTime);
        pb=(ProgressBar)view2.findViewById(R.id.pb);
        tvUpdateTime.setText("上拉刷新");
        pb.setVisibility(View.GONE);
        text_load2.setVisibility(View.GONE);
        lvInfoAct.addFooterView(view2);

        initData();
        loadInfoActData();

        rlDrop.setOnClickListener(this);
        rlUp.setOnClickListener(this);

        // 设置是否可以下拉刷新
       /* refreshView.setPullRefreshEnable(false);
        // 设置是否可以上拉加载
        refreshView.setPullLoadEnable(false);
        // 设置时候可以自动刷新
        refreshView.setAutoRefresh(false);

//        etSearch.addTextChangedListener(watcher);

        refreshView.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {

            @Override
            public void onRefresh() {
                refreshView.stopRefresh();
                initData();
            }

            @Override
            public void onLoadMore(boolean isSlience) {

                new android.os.Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        refreshView.stopLoadMore(true);
                    }
                }, 1000);
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
        });*/
        boolean scrollFlag;
        lvInfoAct.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // TODO Auto-generated method stub
                switch (scrollState) {
                    // 当不滚动时
                    case OnScrollListener.SCROLL_STATE_IDLE:// 是当屏幕停止滚动时
//                        scrollFlag = false;
                        // 判断滚动到底部
                        if (lvInfoAct.getLastVisiblePosition() == (lvInfoAct
                                .getCount() - 1)) {
//                            toTopBtn.setVisibility(View.VISIBLE);
//                            System.out.println("=============滑到底部");
                            ll_refresh.setVerticalGravity(View.VISIBLE);
                            text_load2.setVisibility(View.VISIBLE);
                            tvUpdateTime.setText("正在加载");
                            pb.setVisibility(View.VISIBLE);
                            initData();
                            new android.os.Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    text_load2.setVisibility(View.GONE);
                                }
                            }, 400);
                            new android.os.Handler().postDelayed(new Runnable() {

                                @Override
                                public void run() {
                                    text_load2.setVisibility(View.GONE);
                                    ll_refresh.setVerticalGravity(View.GONE);
                                    tvUpdateTime.setText("上拉刷新");
                                    pb.setVisibility(View.GONE);
                                }
                            }, 1300);
                        }
                        // 判断滚动到顶部
                        if (lvInfoAct.getFirstVisiblePosition() == 0) {
//                            toTopBtn.setVisibility(View.GONE);
                        }

                        break;
                    case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:// 滚动时
//                        scrollFlag = true;
                        break;
                    case OnScrollListener.SCROLL_STATE_FLING:// 是当用户由于之前划动屏幕并抬起手指，屏幕产生惯性滑动时
//                        scrollFlag = false;
                        break;
                }
            }

            /**
             * firstVisibleItem：当前能看见的第一个列表项ID（从0开始）
             * visibleItemCount：当前能看见的列表项个数（小半个也算） totalItemCount：列表项共数
             */
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

//        lvInfoAct.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                if (visibleItemCount + firstVisibleItem == totalItemCount) {
//                    System.out.println("=============滑到底部");
//                }
//            }
//        });
            return view;
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
            initHunt(mContent);
        }
    };

    /**
     * 数据搜索
     */
    public void initHunt(String name) {
        materialInfoBeanList = materialInfoService.getListCustomerInfo(name);
        infoActBaseAdapter.addInfoData(materialInfoBeanList);
        //lvInfoAct.setAdapter(infoActBaseAdapter);
        infoActBaseAdapter.notifyDataSetChanged();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        materialInfoBeanList = materialInfoService.getListCustomerInfo("");
        if (materialInfoBeanList != null && materialInfoBeanList.size() > 0) {
            infoActBaseAdapter.addInfoData(materialInfoBeanList);
            //lvInfoAct.setAdapter(infoActBaseAdapter);
            infoActBaseAdapter.notifyDataSetChanged();
            lvInfoAct.setSelection(ListView.FOCUS_DOWN);//刷新到底部

            pageNumber = (materialInfoBeanList.size() + 4) / 5;
            if (materialInfoBeanList.size() % 5 == 0) {
                ++pageNumber;
            }
            getInformation(1, 11);
        } else {
            getInformation(1, 11);
        }
//        loadInfoActData();
    }

    private void loadInfoActData() {
        AsyncHttpClient httpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        String sPhone = CommonUtil.getUserLoginInfo(getActivity(), Constant.ACCOUNT_CODE);
        params.put("account", sPhone);// 用户登录的账号
        params.put("sign", CommonUtil.md5Encryption(sPhone + Constant.Token));// 签名(md5(account + token))
        httpClient.post(Constant.sMenuListUrl, params, new JsonHttpResponseHandler() {

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
                                    Boolean bl=true;
                                    com.alibaba.fastjson.JSONObject object = (com.alibaba.fastjson.JSONObject) dataArray.get(i);
                                    int iMenuOrder = object.getInteger("menuOrder") == null ? 0 : object.getInteger("menuOrder");
                                    int iMenuStatus = object.getInteger("menuStatus") == null ? 0 : object.getInteger("menuStatus");
                                    String sMenuIcon = object.getString("menuIcon") == null ? "" : object.getString("menuIcon");
                                    String sMenuUrl = object.getString("menuUrl") == null ? "" : object.getString("menuUrl");
                                    String sMenuId = object.getString("menuId") == null ? "" : object.getString("menuId");
                                    String sMenuName = object.getString("menuName") == null ? "" : object.getString("menuName");
                                    int iMenuType = object.getInteger("menuType") == null ? 0 : object.getInteger("menuType");

                                    EotherUrlBean eotherUrlBean = new EotherUrlBean();
                                    eotherUrlBean.setiMenuOrder(iMenuOrder);
                                    eotherUrlBean.setiMenuStatus(iMenuStatus);
                                    eotherUrlBean.setsMenuIcon(sMenuIcon);
                                    eotherUrlBean.setsMenuUrl(sMenuUrl);
                                    eotherUrlBean.setsMenuId(sMenuId);
                                    eotherUrlBean.setsMenuName(sMenuName);
                                    eotherUrlBean.setiMenuType(iMenuType);
                                    for(int y=0;y<eotherUrlBeanList.size();y++){
                                        if(eotherUrlBeanList.get(y).equals(eotherUrlBean)){
                                            bl=false;
                                            break;
                                        }
                                    }
                                    if(bl){
                                        eotherUrlBeanList.add(eotherUrlBean);
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // getInformation(1, 9);
    }

    /**
     * 拿服务器资讯消息
     */
    public void getInformation(long page, int rows) {
        if (!CommonUtil.isNetworkAvailable(getActivity())) {
            CommonUtil.showTips(getActivity(), R.mipmap.warning, "网络不可用");
            return;
        }
        AsyncHttpClient httpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
//        System.out.println(CommonUtil.getUserLoginInfo(getContext(), Constant.ItemId_Code) + "=====IemId");
//        params.put("itemId", CommonUtil.getUserLoginInfo(getContext(), Constant.ItemId_Code));// CommonUtil.getUserLoginInfo(getContext(), Constant.ItemId_Code)
        params.put("page", page + "");// CommonUtil.getUserLoginInfo(getContext(), Constant.ItemId_Code)
        params.put("rows", rows + "");
        params.put("sign", CommonUtil.md5Encryption(CommonUtil.getUserLoginInfo(getActivity(), Constant.ACCOUNT_CODE) + Constant.Token));
        params.put("account", CommonUtil.getUserLoginInfo(getActivity(), Constant.ACCOUNT_CODE));
//        System.out.println(Constant.sInformationUrl + "==" + params);
//        System.out.println(Constant.sInformationUrl + "==" + params);

        httpClient.post(Constant.sInformationUrl, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    String sresponse = String.valueOf(response);
                    com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(sresponse);
                    String msg = jsonObject.getString("message");
                    int code = jsonObject.getInteger("code");
                    com.alibaba.fastjson.JSONObject data = (com.alibaba.fastjson.JSONObject) jsonObject.get("data");

                    if (statusCode == 200) {
                        if (code == 0) {
                            Boolean isInsert = false;
                            if (data != null && data.size() > 0) {
                                JSONArray jsonArray = JSON.parseArray(data.getString("result"));
                                for (int i = 0; i < jsonArray.size(); i++) {
                                    com.alibaba.fastjson.JSONObject sArticles = JSON.parseObject(jsonArray.getString(i));

                                    long lDate = sArticles.getLong("date") == null ? System.currentTimeMillis() : sArticles.getLong("date");
                                    String sitemId = sArticles.getString("itemId") == null ? "" : sArticles.getString("itemId");
                                    int itemType = sArticles.getInteger("itemType") == null ? 0 : sArticles.getInteger("itemType");
                                    int iState = sArticles.getInteger("state") == null ? 0 : sArticles.getInteger("state");
                                    String articles = sArticles.getString("articles") == null ? "" : sArticles.getString("articles");
                                    MaterialInfoBean materialInfoBean = new MaterialInfoBean();
                                    materialInfoBean.setlDate(lDate);
                                    materialInfoBean.setsItemId(sitemId);
                                    materialInfoBean.setItemType(itemType);
                                    materialInfoBean.setiState(iState);
                                    materialInfoBean.setsArticles(articles);

                                    if (materialInfoService.isExists(sitemId) > 0) {// 存在记录就更新
                                        materialInfoService.updateMaterialInfo(materialInfoBean);
                                    } else {// 不存在记录就添加
                                        materialInfoService.insertMaterialInfo(materialInfoBean);
                                        isInsert = true;
                                    }
                                    //materialInfoBeanList.add(materialInfoBean);
                                }
                                //informationBeans
                                // infoActListAdapter.add(materialInfoBeanList);
                                //infoActListAdapter.notifyDataSetChanged();
                                if (isInsert) {
                                    initData();
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                try {

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 消失显示
     */
    public void upDrop(final int code) {
        if (code == 1) {
            rlDrop.setVisibility(View.GONE);
            rlUp.setVisibility(View.VISIBLE);
        } else {
            rlDrop.setVisibility(View.VISIBLE);
            rlUp.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlDrop:// 下
                upDrop(1);
                break;
            case R.id.rlUp:// 上
                upDrop(0);
                showPopuLayout(v);
                break;
        }
    }

    /**
     * 显示下拉的布局
     */
    private void showPopuLayout(View parent) {
        WindowManager windowManager = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        MemberAdapter memberAdapter;
        if (popupWindow == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.drop_layout, null);
            ListView gridview = (ListView) view.findViewById(R.id.gridview);


            // 创建一个PopupWindow对象
            popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            memberAdapter = new MemberAdapter(getActivity(),popupWindow,this);
            memberAdapter.addCity(eotherUrlBeanList);
            gridview.setAdapter(memberAdapter);
            memberAdapter.notifyDataSetChanged();
            // 初始化并且添加点击事件
            // 28会员日
            (view.findViewById(R.id.rlMemberDate)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    popupWindow.dismiss();
                    upDrop(1);
                    CommonUtil.doJumpToPublicWebView(getActivity(), true, "", "", "", "http://www.baidu.com/");
                }
            });
            // 月历宝贝
            (view.findViewById(R.id.rlCalendarMonthBaby)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    popupWindow.dismiss();
                    upDrop(1);
                    CommonUtil.doJumpToPublicWebView(getActivity(), true, "", "", "", "http://www.baidu.com/");
                }
            });
            // NBA优惠
            (view.findViewById(R.id.rlNewPrefer)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    popupWindow.dismiss();
                    upDrop(1);
                    CommonUtil.doJumpToPublicWebView(getActivity(), true, "", "", "", "http://www.baidu.com/");
                }
            });
            // 前线动态
            (view.findViewById(R.id.rlDynamic)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    popupWindow.dismiss();
                    upDrop(1);
                    CommonUtil.doJumpToPublicWebView(getActivity(), true, "", "", "", "http://www.baidu.com/");
                }
            });
            // NBA资讯
            (view.findViewById(R.id.rlNBAInfo)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    popupWindow.dismiss();
                    upDrop(1);
                    CommonUtil.doJumpToPublicWebView(getActivity(), true, "", "", "", "http://www.baidu.com/");
                }
            });
            // 积分兑换
            (view.findViewById(R.id.rlPointsFor)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    popupWindow.dismiss();
                    upDrop(1);
                    CommonUtil.doJumpToPublicWebView(getActivity(), true, "", "", "", "http://www.baidu.com/");
                }
            });
            // 点击消失
            (view.findViewById(R.id.llRoot)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                    upDrop(1);
                }
            });
        }
        // 使其聚集
        popupWindow.setFocusable(true);
        // 设置允许在外点击消失
        popupWindow.setOutsideTouchable(true);
        // 设置动画
        popupWindow.setAnimationStyle(R.style.animationFade);
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        // 显示的位置为:屏幕的宽度的一半-PopupWindow的高度的一半
        int xPos = windowManager.getDefaultDisplay().getWidth() / 2 - popupWindow.getWidth() / 2;
        popupWindow.showAsDropDown(parent, xPos, 0);
    }

}
