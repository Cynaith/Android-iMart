package com.ly.imart.view.Fourth;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocationClient;
import com.ly.imart.R;
import com.ly.imart.bean.Fourth.FourthBean;
import com.ly.imart.maxim.MaximMainActivity;
import com.ly.imart.maxim.bmxmanager.BaseManager;
import com.ly.imart.maxim.bmxmanager.UserManager;
import com.ly.imart.maxim.common.utils.CommonUtils;
import com.ly.imart.maxim.common.utils.SharePreferenceUtils;
import com.ly.imart.maxim.login.view.LoginActivity;
import com.ly.imart.maxim.login.view.WelcomeActivity;
import com.ly.imart.presenter.Fourth.FourthPresenter;
import com.ly.imart.util.CircleImageView;
import com.ly.imart.util.MyImageView;
import com.ly.imart.view.Fourth.ChatView.ChatListActivity;

import java.util.concurrent.ExecutionException;

import im.floo.floolib.BMXErrorCode;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class FourthFragment extends Fragment implements View.OnClickListener, IFourthView {

    private View view;
    private FourthPresenter fourthPresenter;
    CircleImageView circleImageView_mycollection;
    CircleImageView circleImageView_userinfo;
    CircleImageView circleImageView_mail;
    CircleImageView circleImageView_diy;
    CircleImageView circleImageView_myfriends;
    TextView textView_fourth_myfollow;
    TextView textView_fourth_myfollowed;
    TextView textView_myarticle;
    TextView textView_myfriend; //我的获赞数
    TextView textView_username;
    TextView textView_usershow;
    TextView textView_exit;
    RelativeLayout relativeLayout;
    private AMapLocationClient locationClientSingle = null;
    private AMapLocationClient locationClientContinue = null;

    Activity main;

    public void setMainContent(Activity main){
        this.main = main;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fourth_fragment, container, false);

        fourthPresenter = new FourthPresenter(this);

        textView_fourth_myfollow = view.findViewById(R.id.fourth_myfollow);
        textView_fourth_myfollowed = view.findViewById(R.id.fourth_myfollowed);
        textView_myarticle = view.findViewById(R.id.fourth_myarticle);
        textView_myfriend = view.findViewById(R.id.fourth_myfriend);
        (circleImageView_diy = view.findViewById(R.id.fourth_more_diy)).setOnClickListener(this);
        (circleImageView_mycollection = view.findViewById(R.id.fourth_more_mycollection)).setOnClickListener(this);
        (circleImageView_userinfo = view.findViewById(R.id.fourth_more_userinfo)).setOnClickListener(this);
        (circleImageView_mail = view.findViewById(R.id.fourth_more_mail)).setOnClickListener(this);
        relativeLayout = view.findViewById(R.id.fourth_mine);
        textView_username = view.findViewById(R.id.fourth_username);
        textView_usershow = view.findViewById(R.id.fourth_usershow);
        textView_exit = view.findViewById(R.id.exit);
        textView_fourth_myfollow.setClickable(true);
        textView_fourth_myfollowed.setClickable(true);
//        textView_myfriend.setClickable(true);
        textView_myarticle.setClickable(true);
        relativeLayout.setClickable(true);
        textView_exit.setClickable(true);
        textView_fourth_myfollow.setOnClickListener(this);
        textView_fourth_myfollowed.setOnClickListener(this);
        textView_myarticle.setOnClickListener(this);
//        textView_myfriend.setOnClickListener(this); //集成maxim
        textView_exit.setOnClickListener(this);
        relativeLayout.setOnClickListener(this);
//
        initData();

        return view;
    }


    void initData() {
        try {
            FourthBean fourthBean = fourthPresenter.getInfo();
            textView_fourth_myfollow.setText("" + fourthBean.getFollowNum());
            textView_fourth_myfollowed.setText("" + fourthBean.getFollowedNum());
            textView_myarticle.setText("" + fourthBean.getArticleNum());
            textView_username.setText("" + fourthBean.getUserName());
            textView_usershow.setText("" + fourthBean.getUserShow());
            textView_myfriend.setText(""+fourthBean.getSupportNum());
// https://blog.csdn.net/augfun/article/details/86615750
            MyImageView userimage;
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
        switch (view.getId()) {
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
            case R.id.fourth_more_mycollection:
                gotoCollectionPage();
                break;
            case R.id.fourth_more_diy:
                gotoDiyList();
                break;
            /**
             *
             */
            case R.id.fourth_more_userinfo: //设置用户信息
                startActivity(new Intent(this.getActivity(),UpdateUserActivity.class));
                break;
            case R.id.fourth_more_mail: //点赞信息
                startActivity(new Intent(this.getActivity(),SupportList.class));
                break;

            case R.id.exit:
                logout();
                break;
        }
    }



    @Override
    public void gotoFollowPage() {
        Intent intent = new Intent(this.getActivity(), FourthFollowlistActivity.class);
        intent.putExtra("kind", 1);
        intent.putExtra("userName", SharePreferenceUtils.getInstance().getUserName());
        startActivity(intent);
    }

    @Override
    public void gotoFollowedPage() {
        Intent intent = new Intent(this.getActivity(), FourthFollowlistActivity.class);
        intent.putExtra("kind", 2);
        intent.putExtra("userName", SharePreferenceUtils.getInstance().getUserName());
        startActivity(intent);
    }

    @Override
    public void gotoFriendPage() {
        startActivity(new Intent(this.getActivity(), FourthFollowlistActivity.class));
    }

    @Override
    public void gotoArticlePage() {
        Intent intent = new Intent(this.getActivity(), FourthFollowlistActivity.class);
        intent.putExtra("kind", 3);
        intent.putExtra("userName", SharePreferenceUtils.getInstance().getUserName());
        startActivity(intent);
    }
    public void gotoCollectionPage(){
        Intent intent = new Intent(this.getActivity(), FourthFollowlistActivity.class);
        intent.putExtra("kind", 4);
        intent.putExtra("userName", SharePreferenceUtils.getInstance().getUserName());
        startActivity(intent);
    }
    @Override
    public void gotoMyshowPage(String userName) {
        Intent intent = new Intent(this.getActivity(), FourthMyshowActivity.class);
        intent.putExtra("userName", userName);
        startActivity(intent);
//        startActivity(new Intent(this.getActivity(),FourthMyshowActivity.class));

    }

    public void gotoDiyList() {
        Intent intent = new Intent(this.getActivity(), DiyListActivity.class);
        startActivity(intent);
    }

    public void gotoChatList() {
        Intent intent = new Intent(this.getActivity(), ChatListActivity.class);
        startActivity(intent);
    }

    void logout() {
        Observable.just("").map(new Func1<String, BMXErrorCode>() {
            @Override
            public BMXErrorCode call(String s) {
                return UserManager.getInstance().signOut();
            }
        }).flatMap(new Func1<BMXErrorCode, Observable<BMXErrorCode>>() {
            @Override
            public Observable<BMXErrorCode> call(BMXErrorCode errorCode) {
                return BaseManager.bmxFinish(errorCode, errorCode);
            }
        }).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BMXErrorCode>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(BMXErrorCode errorCode) {
                        CommonUtils.getInstance().logout();
                        WelcomeActivity.openWelcome(getActivity());
                    }
                });
        startActivity(new Intent(main, LoginActivity.class));
        main.finish();
    }

    @Override
    public void onResume() {
        initData();
        super.onResume();
    }


}
