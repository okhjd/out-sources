package com.dw.imximeng.activitys;

import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.webkit.DownloadListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.dw.imximeng.R;
import com.dw.imximeng.app.ActivityExtras;
import com.dw.imximeng.base.BaseActivity;
import com.dw.imximeng.helper.ActivityUtils;
import com.dw.imximeng.widgets.ProgressWebView;

import butterknife.BindView;


/**
 * 显示网页
 */
public class WebActivity extends BaseActivity {
    @BindView(R.id.webView)
    ProgressWebView webView;

    String strTitle;
    String strUrl;

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        Bundle bundle = ActivityUtils.getParcelableExtra(this);
        strUrl = bundle.getString(ActivityExtras.EXTRAS_USER_PROTOCOL_URL, "");
        strTitle = bundle.getString(ActivityExtras.EXTRAS_WEB_TITLE, "");
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_web;
    }

    @Override
    public void initView() {
        setTitle(strTitle);
    }

    @Override
    public void initData() {

        webView.setWebViewClient(new WebViewClient() {
            public void onReceivedSslError(WebView view,
                                           SslErrorHandler handler, SslError error) {
                // 　　//handler.cancel(); 默认的处理方式，WebView变成空白页
                handler.proceed(); // 接受证书
                // handleMessage(Message msg); 其他处理

            }
        });
        // 这两行代码一定加上否则效果不会出现
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDefaultTextEncodingName("gb2312");
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                if (url != null && (url.startsWith("http://") || url.startsWith("https://")))
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (strTitle.isEmpty() && title != null) {
                    setTitle(title);
                }
            }
        });
        webView.loadUrl(strUrl);
    }
}
