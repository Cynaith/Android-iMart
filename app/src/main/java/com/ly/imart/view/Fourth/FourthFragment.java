package com.ly.imart.view.Fourth;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ly.imart.R;
import com.ly.imart.presenter.Fourth.FourthPresenter;
import com.ly.imart.util.CircleImageView;

public class FourthFragment extends Fragment implements View.OnClickListener,IFourthView {

    private View view;

    private FourthPresenter fourthPresenter;

//    @BindView(R.id.fourth_more_mycollection)
    CircleImageView circleImageView_mycollection;

//    @BindView(R.id.fourth_more_mygoods)
    CircleImageView circleImageView_mygoods;

//    @BindView(R.id.fourth_more_mycart)
    CircleImageView circleImageView_mycart;

//    @BindView(R.id.fourth_more_diy)
    CircleImageView circleImageView_diy;

//    @BindView(R.id.fourth_more_myfriends)
    CircleImageView circleImageView_myfriends;

//    @BindView(R.id.fourth_myfollow)
    TextView textView_fourth_myfollow;

//    @BindView(R.id.fourth_myfollowed)
    TextView textView_fourth_myfollowed;

//    @BindView(R.id.fourth_myarticle)
    TextView textView_myarticle;

//    @BindView(R.id.fourth_myfriend)
    TextView textView_myfriend;

    RelativeLayout relativeLayout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fourth_fragment, container, false);

        fourthPresenter = new FourthPresenter(this);

        textView_fourth_myfollow = view.findViewById(R.id.fourth_myfollow);
        textView_fourth_myfollowed = view.findViewById(R.id.fourth_myfollowed);
        textView_myarticle = view.findViewById(R.id.fourth_myarticle);
        textView_myfriend = view.findViewById(R.id.fourth_myfriend);
        relativeLayout = view.findViewById(R.id.fourth_mine);

        textView_fourth_myfollow.setClickable(true);
        textView_fourth_myfollowed.setClickable(true);
        textView_myfriend.setClickable(true);
        textView_myarticle.setClickable(true);
        relativeLayout.setClickable(true);
        textView_fourth_myfollow.setOnClickListener(this);
        textView_fourth_myfollowed.setOnClickListener(this);
        textView_myarticle.setOnClickListener(this);
        textView_myfriend.setOnClickListener(this);
        relativeLayout.setOnClickListener(this);
//
        return view;
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
        }
    }

//    private void onClickFourHelp(View view) {
//        Intent intent = new Intent(this.getActivity(), FourthHelpActivity.class);
//        startActivity(intent);
//    }


    @Override
    public void gotoFollowPage() {
        startActivity(new Intent(this.getActivity(),FourthFollowlistActivity.class));
    }

    @Override
    public void gotoFollowedPage() {
        startActivity(new Intent(this.getActivity(),FourthFollowlistActivity.class));
    }

    @Override
    public void gotoFriendPage() {
        startActivity(new Intent(this.getActivity(),FourthFollowlistActivity.class));
    }

    @Override
    public void gotoArticlePage() {
        startActivity(new Intent(this.getActivity(),FourthFollowlistActivity.class));
    }

    @Override
    public void gotoMyshowPage() {
        startActivity(new Intent(this.getActivity(),FourthMyshowActivity.class));
    }
}
