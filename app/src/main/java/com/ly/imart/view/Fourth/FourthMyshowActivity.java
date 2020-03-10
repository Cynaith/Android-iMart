package com.ly.imart.view.Fourth;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ly.imart.R;
import com.ly.imart.adapter.MyshowPageAdapter;
import com.ly.imart.bean.Fourth.MyshowBean;
import com.ly.imart.demo.TabLayout.PageAdapter;
import com.ly.imart.maxim.common.utils.SharePreferenceUtils;
import com.ly.imart.presenter.Fourth.MyshowPresenter;
import com.ly.imart.util.CircleImageView;

import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FourthMyshowActivity extends AppCompatActivity implements IMyshowView, View.OnClickListener {

    @BindView(R.id.fourth_title)
    TextView textView_title;

    @BindView(R.id.activity_myshow_userimg)
    CircleImageView circleImageView_userimg;

    @BindView(R.id.activity_myshow_isfollow)
    Button button_isfollow;

    @BindView(R.id.activity_myshow_message)
    Button button_message;

    @BindView(R.id.activity_myshow_username)
    TextView textView_username;

    @BindView(R.id.activity_myshow_userinfo)
    TextView textView_userinfo;

    @BindView(R.id.activity_myshow_follow)
    TextView textView_follow;

    @BindView(R.id.activity_myshow_followed)
    TextView textView_followed;

    @BindView(R.id.activity_myshow_article)
    TextView textView_article;

    MyshowPresenter myshowPresenter;

    @BindView(R.id.activity_myshow_tabLayout)
    TabLayout tabLayout;

    String userName;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myshow);
        Intent intent = getIntent();
        userName = intent.getStringExtra("userName");
        ButterKnife.bind(this);
        this.setOnclickListener();
        myshowPresenter = new MyshowPresenter(this);
        setTabLayout();
        try {
            initData(userName);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    void initData(String userName) throws ExecutionException, InterruptedException {
        MyshowBean myshowBean = myshowPresenter.initData(userName);
        circleImageView_userimg.setImageURL(myshowBean.getUserimg());
        textView_username.setText(myshowBean.getUsername());
        textView_userinfo.setText(myshowBean.getUserinfo());
        textView_follow.setText(""+myshowBean.getFollowNum());
        textView_followed.setText(""+myshowBean.getFollowedNum());
        textView_article.setText(""+myshowBean.getArticleNum());
        if(userName == SharePreferenceUtils.getInstance().getUserName()){
            button_isfollow.setBackgroundColor(Color.parseColor("#F8F8FF"));
            button_isfollow.setEnabled(false);
        }
        changeFollowButton(myshowBean.isFollow());
    }

    @Override
    public void setOnclickListener() {
        button_isfollow.setOnClickListener(this);
        button_message.setOnClickListener(this);
        textView_follow.setOnClickListener(this);
        textView_followed.setOnClickListener(this);
        textView_article.setOnClickListener(this);
    }

    @Override
    public void changeFollowButton(boolean isFollow) {
        if (isFollow){
            button_isfollow.setText("已关注");
        }
        else{
            button_isfollow.setText("未关注");
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_myshow_isfollow:
                myshowPresenter.followUser("sad");
                break;
            case R.id.activity_myshow_message:
                break;
            case R.id.activity_myshow_follow:
                Intent intent = new Intent(this,FourthFollowlistActivity.class);
                intent.putExtra("kind",1);
                intent.putExtra("userName",userName);
                startActivity(intent);
                break;
            case R.id.activity_myshow_followed:
                Intent intent1 = new Intent(this,FourthFollowlistActivity.class);
                intent1.putExtra("kind",2);
                intent1.putExtra("userName",userName);
                startActivity(intent1);
                break;
            case R.id.activity_myshow_article:
                Intent intent2 = new Intent(this,FourthFollowlistActivity.class);
                intent2.putExtra("kind",3);
                intent2.putExtra("userName",userName);
                startActivity(intent2);
                break;
        }
    }

    @Override
    public void setTabLayout() {
        tabLayout.addTab(tabLayout.newTab().setText("作品"));
        tabLayout.addTab(tabLayout.newTab().setText("动态"));
        tabLayout.addTab(tabLayout.newTab().setText("喜欢"));
        final ViewPager viewPager = (ViewPager) findViewById(R.id.activity_myshow_viewPager);
        viewPager.setAdapter(new MyshowPageAdapter(getSupportFragmentManager(), tabLayout.getTabCount(),userName));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        //绑定tab点击事件
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
