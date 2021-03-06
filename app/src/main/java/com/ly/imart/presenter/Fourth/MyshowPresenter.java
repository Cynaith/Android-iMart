package com.ly.imart.presenter.Fourth;

import android.util.Log;

import com.ly.imart.bean.Fourth.MyshowBean;
import com.ly.imart.maxim.common.utils.SharePreferenceUtils;
import com.ly.imart.model.Fourth.IMyshowModel;
import com.ly.imart.model.Fourth.MyshowModelImpl;
import com.ly.imart.view.Fourth.IMyshowView;

import java.util.concurrent.ExecutionException;

public class MyshowPresenter {

    IMyshowModel iMyshowModel;
    IMyshowView iMyshowView;

    public MyshowPresenter(IMyshowView iMyshowView) {
        this.iMyshowView = iMyshowView;
        iMyshowModel = new MyshowModelImpl();
    }

    public void followUser(String username){
        //do something save to mysql

//        iMyshowModel.setFollow(!iMyshowModel.isFollow());
//        iMyshowView.changeFollowButton(iMyshowModel.isFollow()); //要从model中取相反的boolean
        iMyshowModel.followUser(username);

    }

    public MyshowBean initData(String userName) throws ExecutionException, InterruptedException {
        //从model中获取数据后 通过view的多个界面操作函数来完善界面
//        iMyshowView.changeFollowButton(iMyshowModel.isFollow());

        return iMyshowModel.initData(userName, SharePreferenceUtils.getInstance().getUserName());
    }


}
