package com.livechat.chat.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.livechat.chat.R;
import com.livechat.chat.adapter.GVItemAdapter;
import com.livechat.chat.entity.EotherUrlBean;
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
 * 其他
 * Created by Administrator on 2016/3/21.
 */
public class OtherFragment extends Fragment implements AdapterView.OnItemClickListener {

    //    private CircleImageView cvHeadPic;
//    private TextView tvUserName;
    private GridView gvList;

    //    private EotherUrlService eotherUrlService;
    private GVItemAdapter gvItemAdapter;
    //    private UserLoginInfoService loginInfoService;
    private List<EotherUrlBean> eOtherUrlList;
    private int[] resId = new int[]{
            R.mipmap.deposit,
            R.mipmap.withdrawals,
            R.mipmap.transfer,
            R.mipmap.share,
            R.mipmap.download,
            R.mipmap.score,
            R.mipmap.feed_back
    };

    private ProgressBar progressBar;
    private WebView webView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_other_layout, container, false);

//        cvHeadPic = (CircleImageView) view.findViewById(R.id.cvHeadPic);
//        tvUserName = (TextView) view.findViewById(R.id.tvUserName);
        gvList = (GridView) view.findViewById(R.id.gvList);

        // new add
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        webView = (WebView) view.findViewById(R.id.webView);

        initSetting();

//        loginInfoService = new UserLoginInfoService(getActivity());
        gvItemAdapter = new GVItemAdapter(getContext());
//        eotherUrlService = new EotherUrlService(getContext());
        eOtherUrlList = new ArrayList<>();
        gvList.setOnItemClickListener(this);

        initData();
        return view;
    }

    /**
     * 初始化数据
     */
    private void initData() {
        if (!CommonUtil.isNetworkAvailable(getContext())) {
            CommonUtil.showTips(getContext(), R.mipmap.warning, "网络不可用");
            return;
        }
        AsyncHttpClient httpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        String sPhone = CommonUtil.getUserLoginInfo(getActivity(), Constant.ACCOUNT_CODE);
        params.put("account", sPhone);// 用户登录的账号
        params.put("sign", CommonUtil.md5Encryption(sPhone + Constant.Token));// 签名(md5(account + token))
        httpClient.post(Constant.sOtherMenuListUrl, params, new JsonHttpResponseHandler() {

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

//                                com.alibaba.fastjson.JSONObject object = (com.alibaba.fastjson.JSONObject) dataArray.get(0);
//                                String menuUrl = object.getString("menuUrl") == null ? "http://www.baidu.com" : object.getString("menuUrl");
//                                webView.loadUrl(menuUrl);
//                                webView.loadDataWithBaseURL("about:blank", menuUrl, "text/html", "utf-8", null);

                                for (int i = 0; i < dataArray.size(); i++) {
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
                                    eOtherUrlList.add(eotherUrlBean);
                                }
                                gvItemAdapter.addItem(eOtherUrlList);
                                gvList.setAdapter(gvItemAdapter);
                                gvItemAdapter.notifyDataSetChanged();
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

    /**
     * 单击每一项的事件
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CommonUtil.doJumpToPublicWebView(getActivity(), true, "", "", "", eOtherUrlList.get(position).getsMenuUrl());
    }

    /**
     * 获取初始化数据信息
     */
    @Override
    public void onResume() {
        // 从本地数据库获取
//        UserLoginInfoBean userLoginInfoBean = loginInfoService.getSimpleUserLoginInfo(CommonUtil.getUserLoginInfo(getActivity(), Constant.ACCOUNT_CODE));
//        if (userLoginInfoBean != null) {
//            // 头像
//            CommonUtil.showImageLoader(userLoginInfoBean.getsHeaderImage().equals("") ? "" : userLoginInfoBean.getsHeaderImage(), cvHeadPic, R.mipmap.head);
//            // 昵称
//            tvUserName.setText(userLoginInfoBean.getsNickname().equals("") ? "未设置" : userLoginInfoBean.getsNickname());
//        }
        super.onResume();
    }

    private void initSetting() {
        WebSettings wvSettings = webView.getSettings();
        wvSettings.setJavaScriptEnabled(true);// 支持js
        wvSettings.setDefaultTextEncodingName("UTF-8");
//        wvSettings.setUseWideViewPort(false);// WebView双击变大,再双击后变小,当手动放大后,双击可以恢复到原始大小
        wvSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);// 加速WebView加载的方法,提高渲染的优先级
        wvSettings.setSavePassword(false);
        wvSettings.setSaveFormData(false);

        wvSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        wvSettings.setLoadWithOverviewMode(true);// 页面适应手机屏幕的分辨率,完整的显示在屏幕上,可以放大缩小
        wvSettings.setSupportZoom(false);// 仅支持双击缩放,不支持触摸缩放(4.0)
        wvSettings.setBuiltInZoomControls(false);// 是否可缩放

        webView.setHorizontalScrollBarEnabled(false);// 水平不显示
        webView.setVerticalScrollBarEnabled(false); // 垂直不显示
        CookieManager.getInstance().setAcceptCookie(true);

        webView.requestFocus();// 触摸焦点起作用
        // 优先使用缓存(LOAD_CACHE_ELSE_NETWORK);不使用缓存(LOAD_NO_CACHE)
        wvSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        wvSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.clearCache(true);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                progressBar.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(View.GONE);
                super.onPageFinished(view, url);
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {// 页面完成加载
                    progressBar.setVisibility(View.GONE);
                } else {// 正在加载中...
                    progressBar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
        });

        webView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 隐藏输入法
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                // 显示或者隐藏输入法
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });
    }

}
