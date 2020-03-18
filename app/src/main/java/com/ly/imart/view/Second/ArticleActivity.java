package com.ly.imart.view.Second;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.ly.imart.R;
import com.ly.imart.bean.Second.ArticleBean;
import com.ly.imart.model.Second.ArticleMiddleModel;
import com.ly.imart.model.Second.ArticleModel;
import com.ly.imart.util.CircleImageView;
import com.ly.imart.view.Fourth.FourthMyshowActivity;
import com.varunest.sparkbutton.SparkButton;

import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.concurrent.ExecutionException;

public class ArticleActivity extends AppCompatActivity implements View.OnClickListener {

    static int articleId;

    private TextView title;
    private CircleImageView userImg;
    private TextView username;
    private TextView userInfo;
    private HtmlTextView htmlTextView;
    private SparkButton support;
    private boolean isSupport = false;
    private SparkButton collection;
    private boolean isCollection = false;
    private static String string_title;
    private static String string_userImg;
    private static String string_username;
    private static String string_userInfo;
    private static String html;
    private ArticleMiddleModel articleMiddleModel;

    public static void openArticleById(Context context, int id) {
        articleId = id;
        Intent intent = new Intent(context, ArticleActivity.class);
        context.startActivity(intent);
        //未完成：Article不见时finsh；
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        articleMiddleModel = new ArticleMiddleModel();
        initView();
// loads html from string and displays http://www.example.com/cat_pic.png from the Internet
//        htmlTextView.setHtml("<h2>Hello wold</h2><img src=\"http://47.101.171.252:81/static/0b24daae-334d-4cbf-8c0a-4ab63ba979ec.jpg\"/>",
//                new HtmlHttpImageGetter(htmlTextView));
    }

    void initView() {
        title = findViewById(R.id.article_title);
        userImg = findViewById(R.id.article_userimg);
        username = findViewById(R.id.article_username);
        userInfo = findViewById(R.id.article_userinfo);
        htmlTextView = findViewById(R.id.html_text);
        support = findViewById(R.id.star_button);
        collection = findViewById(R.id.spark_button);

        /**
         * 此处未获取数据
         */
        support.setChecked(isSupport);
        collection.setChecked(isCollection);
//        collection = new SparkButtonBuilder(this)
//                .setActiveImage(R.drawable.article_collection_on)
//                .setInactiveImage(R.drawable.article_collection_off)
//                .setImageSizePx(35)
//                .setPrimaryColor(ContextCompat.getColor(this, R.color.imart_primary_color))
//                .setSecondaryColor(ContextCompat.getColor(this, R.color.imart_secondary_color))
//                .build();
        bindClick();
        title.setText(string_title);
        userImg.setImageURL(string_userImg);
        username.setText(string_username);
        userInfo.setText(string_userInfo);
        htmlTextView.setListIndentPx(10);
        htmlTextView.setHtml(html, new HtmlHttpImageGetter(htmlTextView));

    }

    private void bindClick() {
        userImg.setOnClickListener(this);
        support.setOnClickListener(this);
        collection.setOnClickListener(this);
    }

    public static void initData(int id) throws ExecutionException, InterruptedException {
        ArticleModel articleModel = new ArticleModel();
        ArticleBean articleBean = articleModel.getArticle(id);
        string_title = articleBean.getTitle();
        string_username = articleBean.getUsername();
        string_userImg = articleBean.getUserImg();
        string_userInfo = articleBean.getUserInfo();
        html = articleBean.getText();
        html = html.replaceAll("src:", "src=");
        html = html.replaceAll("'", "\"");
        System.out.println(html);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.article_userimg:
                Intent intent = new Intent(this, FourthMyshowActivity.class);
                intent.putExtra("userName", string_username);
                startActivity(intent);
                break;
            case R.id.star_button://点赞按钮
                try {
                    articleMiddleModel.action(articleId, 1);
                    if (!isSupport) support.playAnimation();
                    support.setChecked(isSupport = !isSupport);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.spark_button://收藏按钮
                try {
                    articleMiddleModel.action(articleId, 2);
                    if (!isCollection) collection.playAnimation();
                    collection.setChecked(isCollection = !isCollection);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
