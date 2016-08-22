package com.livechat.chat;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.livechat.chat.utils.CommonUtil;

/**
 * 公共的WebView页面显示
 */
public class PublicWebViewActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private RelativeLayout mRlActionBar;
    private ImageButton imgBtnBack;
    private Button btnWvOther;
    private TextView tvWvTitle, tvWvMasterTitle, tvWarm;
    private ImageView imgTitle;
    private WebView webView;
    private ProgressBar progressBar;
    private SwipeRefreshLayout mSwipeRefresh;
    private String masterTitle = "";

    private String sWebViewUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_web_view);

        initUI();
        initWebViewSetting();
    }

    /**
     * 初始化控件
     */
    private void initUI() {
        mRlActionBar = (RelativeLayout) this.findViewById(R.id.rlTitle);
        imgBtnBack = (ImageButton) this.findViewById(R.id.ivBack);
        imgBtnBack.setOnClickListener(this);
        tvWvTitle = (TextView) this.findViewById(R.id.tvWvTitle);
        tvWvMasterTitle = (TextView) this.findViewById(R.id.tvWvMasterTitle);
        btnWvOther = (Button) this.findViewById(R.id.btnWvOther);
        imgTitle = (ImageView) this.findViewById(R.id.img_title_oter);
        progressBar = (ProgressBar) this.findViewById(R.id.progressBar);
        mSwipeRefresh = (SwipeRefreshLayout) this.findViewById(R.id.swipeRefresh);
        webView = (WebView) this.findViewById(R.id.webView);
        tvWarm = (TextView) this.findViewById(R.id.tvWarm);
        // 获取内容
        Bundle bundle = this.getIntent().getExtras();
        boolean whetherShow = bundle.getBoolean("whetherShowActionBar");
        String subTitle = bundle.getString("subTitle");
        masterTitle = bundle.getString("masterTitle");
        String otherTitle = bundle.getString("otherTitle");
        if (TextUtils.isEmpty(masterTitle)) {
            imgTitle.setVisibility(View.VISIBLE);
            tvWvMasterTitle.setVisibility(View.GONE);
        }
        sWebViewUrl = bundle.getString("webViewUrl");
        if (whetherShow) {// true:显示
            mRlActionBar.setVisibility(View.VISIBLE);
        } else {// false:隐藏
            mRlActionBar.setVisibility(View.GONE);
        }
        // 为控件设置值
        tvWvTitle.setText(subTitle);
        tvWvMasterTitle.setText(masterTitle);
        btnWvOther.setText(otherTitle);
        // 下拉刷新事件
        mSwipeRefresh.setOnRefreshListener(this);
        CommonUtil.setSwipeRefreshBg(mSwipeRefresh);
    }

    /**
     * 初始化WebView的设置
     */
    private void initWebViewSetting() {
        if (TextUtils.isEmpty(sWebViewUrl)) {
            tvWarm.setVisibility(View.VISIBLE);
            webView.setVisibility(View.GONE);
            return;
        }
        if (!CommonUtil.isNetworkAvailable(this)) {
            CommonUtil.showTips(this, R.mipmap.warning, "网络不可用");
            webView.loadUrl(sWebViewUrl);
            return;
        }
        WebSettings wvSettings = webView.getSettings();
        wvSettings.setJavaScriptEnabled(true);// 支持js
        wvSettings.setDefaultTextEncodingName("UTF-8");
        wvSettings.setUseWideViewPort(true);// WebView双击变大,再双击后变小,当手动放大后,双击可以恢复到原始大小
        wvSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);// 加速WebView加载的方法,提高渲染的优先级
        wvSettings.setSavePassword(false);
        wvSettings.setSaveFormData(false);

        wvSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        wvSettings.setLoadWithOverviewMode(true);// 页面适应手机屏幕的分辨率,完整的显示在屏幕上,可以放大缩小
        wvSettings.setSupportZoom(false);// 仅支持双击缩放,不支持触摸缩放(4.0)
        wvSettings.setBuiltInZoomControls(false);// 是否可缩放

        webView.setHorizontalScrollBarEnabled(false);// 水平不显示
        webView.setVerticalScrollBarEnabled(false); // 垂直不显示
        CookieManager.getInstance().setAcceptCookie(true);

        webView.requestFocus();// 触摸焦点起作用
        // 优先使用缓存(LOAD_CACHE_ELSE_NETWORK);不使用缓存(LOAD_NO_CACHE)
//        wvSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        wvSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
//        webView.clearCache(true);
        // 加载传递过来的参数Url
        if (masterTitle.equals("资讯")) {
            //webView.loadData(sWebViewUrl,"text/html","UTF-8");
            webView.loadDataWithBaseURL("about:blank", sWebViewUrl, "text/html", "utf-8", null);
//            System.out.println("======"+sWebViewUrl);
        } else {
            webView.loadUrl(sWebViewUrl);
        }

        // 设置事件
        // 覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为,使网页用WebView打开
        webView.setWebViewClient(new WebViewClient() {

            /**
             * 做地址的拦截操作
             *
             * @param view
             * @param url
             * @return true:控制去WebView打开(拦截地址);false:调用系统浏览器或第三方浏览器(放行通过)
             */
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);
                return false;
            }

            /**
             * 开始加载页面
             *
             * @param view
             * @param url
             * @param favicon
             */
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                progressBar.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);
            }

            /**
             * 加载结束页面
             *
             * @param view
             * @param url
             */
            @Override
            public void onPageFinished(WebView view, String url) {
                // 隐藏进度条
                progressBar.setVisibility(View.GONE);
                mSwipeRefresh.setRefreshing(false);
                super.onPageFinished(view, url);
            }

            /**
             * 网络错误或页面丢失时
             *
             * @param view
             * @param request
             * @param error
             */
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }
        });
        // 判断页面加载过程
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
        //webView.loadData();
    }

    /**
     * 刷新操作
     */
    @Override
    public void onRefresh() {
        if (!CommonUtil.isNetworkAvailable(this)) {
            CommonUtil.showTips(this, R.mipmap.warning, "网络不可用");
            mSwipeRefresh.setRefreshing(false);
            webView.loadUrl(sWebViewUrl);
            return;
        }
        if (masterTitle.equals("资讯")) {
            //webView.loadData(sWebViewUrl,"text/html","UTF-8");
            webView.loadDataWithBaseURL("about:blank", sWebViewUrl, "text/html", "utf-8", null);
//            System.out.println("======"+sWebViewUrl);
        } else {
            webView.loadUrl(sWebViewUrl);
        }
    }

    /**
     * 右边按钮的单击事件
     *
     * @param view
     */
    public void doOtherBtnClick(View view) {

    }

    /**
     * 按返回键的设置
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                // 返回上一个页面
                webView.goBack();
                return true;
            } else {// 退出当前Activity
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                if (webView.canGoBack()) {
                    // 返回上一个页面
                    webView.goBack();
                } else {// 退出当前Activity
                    finish();
                }
                break;

            default:
                break;
        }

    }
}
