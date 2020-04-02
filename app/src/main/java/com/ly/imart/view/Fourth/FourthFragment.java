package com.ly.imart.view.Fourth;


import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.ly.imart.R;
import com.ly.imart.bean.Fourth.FourthBean;
import com.ly.imart.maxim.MaximMainActivity;
import com.ly.imart.maxim.common.utils.SharePreferenceUtils;
import com.ly.imart.maxim.login.view.MineFragment;
import com.ly.imart.maxim.message.view.SessionFragment;
import com.ly.imart.presenter.Fourth.FourthPresenter;
import com.ly.imart.util.CircleImageView;
import com.ly.imart.util.MyImageView;
import com.ly.imart.util.SystemInfo;
import com.ly.imart.view.Fourth.GaodeMap.util.Utils;

import java.net.URL;
import java.util.concurrent.ExecutionException;

import im.floo.floolib.BMXUserProfile;

public class FourthFragment extends Fragment implements View.OnClickListener,IFourthView {

    private View view;
    private FourthPresenter fourthPresenter;
    CircleImageView circleImageView_mycollection;
    CircleImageView circleImageView_mygoods;
    CircleImageView circleImageView_mycart;
    CircleImageView circleImageView_diy;
    CircleImageView circleImageView_myfriends;
    TextView textView_fourth_myfollow;
    TextView textView_fourth_myfollowed;
    TextView textView_myarticle;
    TextView textView_myfriend;
    TextView textView_username;
    TextView textView_usershow;
    RelativeLayout relativeLayout;
    private AMapLocationClient locationClientSingle = null;
    private AMapLocationClient locationClientContinue = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fourth_fragment, container, false);

        fourthPresenter = new FourthPresenter(this);

        textView_fourth_myfollow = view.findViewById(R.id.fourth_myfollow);
        textView_fourth_myfollowed = view.findViewById(R.id.fourth_myfollowed);
        textView_myarticle = view.findViewById(R.id.fourth_myarticle);
        textView_myfriend = view.findViewById(R.id.fourth_myfriend);
        (circleImageView_myfriends = view.findViewById(R.id.fourth_more_myfriends)).setOnClickListener(this);
        (circleImageView_diy = view.findViewById(R.id.fourth_more_diy)).setOnClickListener(this);
        (circleImageView_mycollection =view.findViewById(R.id.fourth_more_mycollection)).setOnClickListener(this);
        (circleImageView_mygoods = view.findViewById(R.id.fourth_more_mygoods)).setOnClickListener(this);
        relativeLayout = view.findViewById(R.id.fourth_mine);
        textView_username = view.findViewById(R.id.fourth_username);
        textView_usershow = view.findViewById(R.id.fourth_usershow);
        textView_fourth_myfollow.setClickable(true);
        textView_fourth_myfollowed.setClickable(true);
        textView_myfriend.setClickable(true);
        textView_myarticle.setClickable(true);
        relativeLayout.setClickable(true);
        textView_fourth_myfollow.setOnClickListener(this);
        textView_fourth_myfollowed.setOnClickListener(this);
        textView_myarticle.setOnClickListener(this);
        textView_myfriend.setOnClickListener(this); //集成maxim

        relativeLayout.setOnClickListener(this);
//
        initData();

        return view;
    }


    void initData(){
        try {
            FourthBean fourthBean = fourthPresenter.getInfo();
            textView_fourth_myfollow.setText(""+fourthBean.getFollowNum());
            textView_fourth_myfollowed.setText(""+fourthBean.getFollowedNum());
            textView_myarticle.setText(""+fourthBean.getArticleNum());
            textView_username.setText(""+fourthBean.getUserName());
            textView_usershow.setText(""+fourthBean.getUserShow());
// https://blog.csdn.net/augfun/article/details/86615750
            MyImageView userimage ;
            userimage = view.findViewById(R.id.fourth_userimage);
            System.out.println(fourthBean.getImgUrl());
            userimage.setImageURL(fourthBean.getImgUrl());
//            textView_myfriend.setText(SharePreferenceUtils.getInstance());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
//            case R.id.four_help:
//                onClickFourHelp(view);
//                break;
            case R.id.fourth_myfollow:
                fourthPresenter.gotoFollowPage();
                break;
            case R.id.fourth_myfollowed:
                fourthPresenter.gotoFollowedPage();
                break;
            case R.id.fourth_myarticle:
                fourthPresenter.gotoArticlePage();
                break;
            case R.id.fourth_myfriend:
                fourthPresenter.gotoFriendPage();
                break;
            case R.id.fourth_mine:
                fourthPresenter.gotoMyshowPage();
                break;
            case R.id.fourth_more_diy:
                gotoDiyList();
                break;
            case R.id.fourth_more_mygoods:
                gotoChatList();
                break;
            case R.id.fourth_more_myfriends:
                MaximMainActivity.openMain(this.getContext());
//                Intent intent = new Intent(this.getContext(),MaximMainActivity.class);
//                this.startActivity(intent);
                break;
        }
    }

//    private void onClickFourHelp(View view) {
//        Intent intent = new Intent(this.getActivity(), FourthHelpActivity.class);
//        startActivity(intent);
//    }


    @Override
    public void gotoFollowPage() {
        Intent intent = new Intent(this.getActivity(),FourthFollowlistActivity.class);
        intent.putExtra("kind",1);
        intent.putExtra("userName",SharePreferenceUtils.getInstance().getUserName());
        startActivity(intent);
    }

    @Override
    public void gotoFollowedPage() {
        Intent intent = new Intent(this.getActivity(),FourthFollowlistActivity.class);
        intent.putExtra("kind",2);
        intent.putExtra("userName",SharePreferenceUtils.getInstance().getUserName());
        startActivity(intent);
    }

    @Override
    public void gotoFriendPage() {
        startActivity(new Intent(this.getActivity(),FourthFollowlistActivity.class));
    }

    @Override
    public void gotoArticlePage() {
        Intent intent = new Intent(this.getActivity(),FourthFollowlistActivity.class);
        intent.putExtra("kind",3);
        intent.putExtra("userName",SharePreferenceUtils.getInstance().getUserName());
        startActivity(intent);
    }

    @Override
    public void gotoMyshowPage(String userName) {
        Intent intent = new Intent(this.getActivity(),FourthMyshowActivity.class);
        intent.putExtra("userName",userName);
        startActivity(intent);
//        startActivity(new Intent(this.getActivity(),FourthMyshowActivity.class));

    }

    public void gotoDiyList(){
        Intent intent = new Intent(this.getActivity(),DiyListActivity.class);
        startActivity(intent);
    }

    public void gotoChatList(){
        Intent intent = new Intent(this.getActivity(), ChatListActivity.class);
        startActivity(intent);
    }
}
