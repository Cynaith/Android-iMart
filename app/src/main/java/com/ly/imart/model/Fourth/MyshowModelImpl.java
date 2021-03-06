package com.ly.imart.model.Fourth;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.ly.imart.bean.Fourth.FourthBean;
import com.ly.imart.bean.Fourth.FriendListBean;
import com.ly.imart.bean.Fourth.MyshowBean;
import com.ly.imart.bean.Response.ResponseBean;
import com.ly.imart.maxim.common.utils.SharePreferenceUtils;
import com.ly.imart.util.OkHttpRequest;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MyshowModelImpl implements IMyshowModel {
    private static final String ACTIVITY_TAG = "MyshowModelImpl";

    MyshowBean myshowBean;

    public MyshowModelImpl() {
        myshowBean = new MyshowBean();
    }



    @Override
    public MyshowBean initData(String userName,String loginName) throws ExecutionException, InterruptedException {
        //从p层传id  从mysql获取数据传入Bean
//        myshowBean.setFollow(true);

        ExecutorService es = Executors.newSingleThreadExecutor();
        Future<String> future = es.submit(new  getResponseData(userName,loginName));
        ResponseBean responseBean = JSON.parseObject(future.get(),ResponseBean.class);
        if (future.isDone()) {

            es.shutdown();
        }
        String data = responseBean.getData().toString().replaceAll("=",":");
        data = data.replaceAll("\"","\'");

        MyshowBean myshowBean = JSON.parseObject(data,MyshowBean.class);
        return myshowBean;

    }
    @Override
    public void followUser(String userName) {
        ExecutorService es = Executors.newSingleThreadExecutor();
        Future<String> future = es.submit(new followuser(userName));
    }
    @Override
    public boolean isFollow() {
        return myshowBean.isFollow();
    }

    @Override
    public void setFollow(boolean follow) {
        //修改数据库中数据，修改 关注表
        myshowBean.setFollow(follow);
    }


    class getResponseData implements Callable<String> {

        public getResponseData(String userName, String loginName) {
            this.userName = userName;
            this.loginName = loginName;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getLoginName() {
            return loginName;
        }

        public void setLoginName(String loginName) {
            this.loginName = loginName;
        }

        //向后台传的参数
        String userName;
        String loginName;



        @Override
        public String call() throws Exception {
            String accessToken = "";
            OkHttpRequest okHttpRequest = new OkHttpRequest();
            String accessTokenUrl = "http://47.101.171.252:8080/user/myshow?userName="+userName+"&loginName="+loginName ;
            try {
                //发送请求
                accessToken = okHttpRequest.get(accessTokenUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d(ACTIVITY_TAG,accessToken);
            return accessToken;
        }
    }
    class followuser implements Callable<String> {

        String userName;

        public followuser(String userName) {
            this.userName = userName;
        }

        @Override
        public String call() throws Exception {
            String accessToken = "";
            OkHttpRequest okHttpRequest = new OkHttpRequest();
            String accessTokenUrl = "http://47.101.171.252:8080/user/followUser?followName="+userName+"&userName="+ SharePreferenceUtils.getInstance().getUserName();
            try {
                //发送请求
                accessToken = okHttpRequest.get(accessTokenUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d(ACTIVITY_TAG,accessToken);
            return accessToken;
        }
    }
}
