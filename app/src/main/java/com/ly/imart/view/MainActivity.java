package com.ly.imart.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.ly.imart.R;
import com.ly.imart.maxim.message.view.SessionFragment;
import com.ly.imart.view.First.FirstFragment;
import com.ly.imart.view.Fourth.FourthFragment;
import com.ly.imart.view.Others.AddArticleTitle;
import com.ly.imart.view.Others.AddQuestion;
import com.ly.imart.view.Others.AddVideo;
import com.ly.imart.view.Second.SecondFragment;
import com.ly.imart.view.Third.ThirdFragment;
import com.next.easynavigation.constant.Anim;
import com.next.easynavigation.view.EasyNavigationBar;

import java.util.ArrayList;
import java.util.List;

import lrq.com.addpopmenu.PopMenu;
import lrq.com.addpopmenu.PopMenuItem;
import lrq.com.addpopmenu.PopMenuItemListener;

public class MainActivity extends AppCompatActivity {

//    https://www.jianshu.com/p/ce8e09cda486

//    在线SVG http://inloop.github.io/svg2android/

    private EasyNavigationBar navigationBar;

    private String[] tabText = { "发现","聊天", "", "视频", "我的"};
    //未选中icon
    private int[] normalIcon = {R.drawable.ic_kind_unselect,R.drawable.ic_chat_unselect,  R.drawable.ic_addto, R.drawable.ic_video_unselect, R.drawable.ic_mine_unselect};
    //选中时icon
    private int[] selectIcon = {R.drawable.ic_kind_select,R.drawable.ic_chat_select,  R.drawable.ic_addto, R.drawable.ic_video_select, R.drawable.ic_mine_select};

    private List<Fragment> fragments = new ArrayList<>();

    private Handler mHandler = new Handler();


    private boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        //getSupportActionBar().hide();
//去掉最上面时间、电量等
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
//                , WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        View view = LayoutInflater.from(this).inflate(R.layout.custom_add_view, null);


        navigationBar = findViewById(R.id.navigationBar);

//        fragments.add(new FirstFragment());

        fragments.add(new SecondFragment());
        fragments.add(new SessionFragment());
        fragments.add(new ThirdFragment());
        Fragment fourthFragment = new FourthFragment();
        fragments.add(fourthFragment);
        ((FourthFragment) fourthFragment).setMainContent(this);

        navigationBar.titleItems(tabText)
                .normalIconItems(normalIcon)
                .selectIconItems(selectIcon)
                .normalTextColor(Color.parseColor("#666666"))
                .selectTextColor(Color.parseColor("#aaaaaa"))
                .fragmentList(fragments)
                .fragmentManager(getSupportFragmentManager())
                .addLayoutRule(EasyNavigationBar.RULE_BOTTOM)
                .addLayoutBottom(100)
                .mode(EasyNavigationBar.MODE_ADD)
                .anim(Anim.ZoomIn)
                .canScroll(true)
                .onTabClickListener(new EasyNavigationBar.OnTabClickListener() {
                    @Override
                    public boolean onTabClickEvent(View view, int position) {
                        Log.e("Tap->Position", position + "");
                        if (position == 2) {

                            //                            https://github.com/joelan/WeiboPopupMenu

                            PopMenu mPopMenu = new PopMenu.Builder().attachToActivity(MainActivity.this)
                                    .addMenuItem(new PopMenuItem("添加文章", getResources().getDrawable(R.drawable.ic_addto_article)))
                                    .addMenuItem(new PopMenuItem("软件反馈", getResources().getDrawable(R.drawable.ic_addto_product)))
                                    .addMenuItem(new PopMenuItem("添加视频", getResources().getDrawable(R.drawable.ic_addto_video)))
                                    .setOnItemClickListener(new PopMenuItemListener() {
                                        @Override
                                        public void onItemClick(PopMenu popMenu, int position) {
                                            if (position == 0) {
                                                gotoAddArticleActivity();
                                            }
                                            if (position ==1){
                                                gotoQuestionActivity();
                                            }
                                            if (position == 2) {
                                                gotoAddVideoActivity();
                                            }
//                                            Toast.makeText(MainActivity.this, "你点击了第" + position + "个位置", Toast.LENGTH_SHORT).show();


                                        }
                                    })
                                    .build();

                            mPopMenu.setmMarginTopRemainSpace(1.2f);
                            mPopMenu.setmIsmalpositionAnimatOut(true);
                            mPopMenu.show();

                            //                            https://github.com/joelan/WeiboPopupMenu
                        }
                        return false;
                    }
                })
                .build();

    }

    public EasyNavigationBar getNavigationBar() {
        return navigationBar;
    }

    private void gotoAddArticleActivity() {
        startActivity(new Intent(this, AddArticleTitle.class));
    }

    private void gotoAddVideoActivity() {
        startActivity(new Intent(this, AddVideo.class));
    }
    private void gotoQuestionActivity(){
        startActivity(new Intent(this, AddQuestion.class));
    }
    public static void openIMart(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        ((Activity) context).finish();
    }

}
