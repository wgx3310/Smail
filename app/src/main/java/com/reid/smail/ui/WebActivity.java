package com.reid.smail.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.reid.smail.R;
import com.reid.smail.content.AccountManager;
import com.reid.smail.content.Constant;
import com.reid.smail.content.Reminder;
import com.reid.smail.content.SettingKey;

public class WebActivity extends BaseActivity {

    private Toolbar mToolbar;
    private ProgressBar mProgressBar;
    private WebView mWebView;

    private String mExtraTitle;
    private String mUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        handleIntent();
        initToolbar();

        initView();
    }

    private void handleIntent() {
        Intent intent = getIntent();
        if (intent != null){
            mExtraTitle = intent.getStringExtra(SettingKey.KEY_TITLE);
            mUrl = intent.getStringExtra(SettingKey.KEY_URL);
        }
    }

    private void initView() {
        mProgressBar = findViewById(R.id.progress_bar);
        mProgressBar.setProgress(0);

        mWebView = findViewById(R.id.web_view);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.setWebChromeClient(mChromeClient);
        mWebView.setWebViewClient(mViewClient);

        if (!TextUtils.isEmpty(mUrl)){
            mWebView.loadUrl(mUrl);
        }
    }

    private void initToolbar() {
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(mExtraTitle);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private WebChromeClient mChromeClient = new WebChromeClient(){
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (TextUtils.isEmpty(mExtraTitle)){
                setTitle(title);
            }
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            mProgressBar.setVisibility(View.VISIBLE);
            mProgressBar.setProgress(newProgress);
            if (newProgress >= 100){
                mProgressBar.setVisibility(View.GONE);
            }
        }
    };

    private WebViewClient mViewClient = new WebViewClient(){
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            Uri uri = request.getUrl();
            if (Constant.SCHEMA.equals(uri.getScheme())){
                String code = uri.getQueryParameter("code");
                String state = uri.getQueryParameter("state");
                if (!TextUtils.isEmpty(code) && Constant.OAUTH_STATE.equals(state)){
                    AccountManager.get().acquireAccessToken(code);
                }else {
                    Reminder.toast(R.string.login_failed);
                }
                mWebView.stopLoading();
                finish();
                return true;
            }

            return super.shouldOverrideUrlLoading(view, request);
        }
    };

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if (mWebView != null && mWebView.canGoBack()){
                mWebView.goBack();
                return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    protected void onDestroy() {
        if (mWebView != null){
            mWebView.stopLoading();
            mWebView.clearCache(true);
            mWebView.clearHistory();
            mWebView.setWebViewClient(null);
            mWebView.setWebChromeClient(null);
            mWebView.freeMemory();
            mWebView.pauseTimers();
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
    }
}
