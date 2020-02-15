package com.ly.imart.view.Second;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.ly.imart.R;
import com.ly.imart.bean.Second.ArticleMainBean;
import com.ly.imart.presenter.Second.ArticleMainPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Webview https://blog.csdn.net/LI_YU_CSDN/article/details/80490771
 */

public class ArticleMainActivity extends AppCompatActivity implements IArticleMainView {
    @BindView(R.id.activity_article_main_webview)
    WebView webView;

    private ArticleMainPresenter articleMainPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_main);
        articleMainPresenter = new ArticleMainPresenter(this);
        ButterKnife.bind(this);
        articleMainPresenter.getUserInfo("userphone");

    }


    @Override
    public void initView(ArticleMainBean articleMainBean) {
        WebSettings webSettings = webView.getSettings();
//        webSettings.setJavaScriptEnabled(true);
//        webSettings.setUseWideViewPort(true);
//        webSettings.setLoadWithOverviewMode(true);
//        webSettings.setBuiltInZoomControls(true);
//        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
//        webView.setWebViewClient(new WebViewClient());
        int screenDensity = getResources().getDisplayMetrics().densityDpi;
        WebSettings.ZoomDensity zoomDensity = WebSettings.ZoomDensity.MEDIUM;
        switch (screenDensity) {
            case DisplayMetrics.DENSITY_LOW:
                zoomDensity = WebSettings.ZoomDensity.CLOSE;
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                zoomDensity = WebSettings.ZoomDensity.MEDIUM;
                break;
            case DisplayMetrics.DENSITY_HIGH:
                zoomDensity = WebSettings.ZoomDensity.FAR;
                break;
        }
        webSettings.setDefaultZoom(zoomDensity);
        String Url = "<html><body>" + articleMainBean.getContent() + "</body></html>";
        webView.loadDataWithBaseURL(null, Url, "text/html", "utf-8", null);
    }
}
