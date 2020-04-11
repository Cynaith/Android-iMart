package com.ly.imart.model.Fourth;

import com.ly.imart.bean.Fourth.MyshowBean;

import java.util.concurrent.ExecutionException;

public interface IMyshowModel {
    MyshowBean initData(String userName, String loginName) throws ExecutionException, InterruptedException;
    boolean isFollow();
    void setFollow(boolean follow);
    void followUser(String userName);
}
