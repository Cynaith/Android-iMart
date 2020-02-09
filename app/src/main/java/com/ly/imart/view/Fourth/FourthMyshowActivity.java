package com.ly.imart.view.Fourth;

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
import com.ly.imart.demo.TabLayout.PageAdapter;
import com.ly.imart.presenter.Fourth.MyshowPresenter;
import com.ly.imart.util.CircleImageView;

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
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myshow);
        ButterKnife.bind(this);
        this.setOnclickListener();
        myshowPresenter = new MyshowPresenter(this);
        myshowPresenter.initData();
        setTabLayout();

    }


    @Override
    public void setOnclickListener() {
        button_isfollow.setOnClickListener(this);
        button_message.setOnClickListener(this);
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
        }
    }

    @Override
    public void setTabLayout() {
        tabLayout.addTab(tabLayout.newTab().setText("作品"));
        tabLayout.addTab(tabLayout.newTab().setText("动态"));
        tabLayout.addTab(tabLayout.newTab().setText("喜欢"));
        final ViewPager viewPager = (ViewPager) findViewById(R.id.activity_myshow_viewPager);
        viewPager.setAdapter(new MyshowPageAdapter(getSupportFragmentManager(), tabLayout.getTabCount()));
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
