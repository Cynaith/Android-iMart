package com.ly.imart.model.Fourth;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.TypeReference;
import com.ly.imart.bean.Fourth.FriendListBean;
import com.ly.imart.bean.Fourth.UserPageBean;
import com.ly.imart.bean.Response.ResponseBean;
import com.ly.imart.util.OkHttpRequest;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FollowListModelImpl implements IFollowListModel {
    private static final String ACTIVITY_TAG = "FollowListModelImpl";

    private UserPageBean userPageBean;

    public FollowListModelImpl() {
        userPageBean = new UserPageBean();
    }

    @Override
    public UserPageBean getUserInfoByUsername(String username) {
        return userPageBean;
    }


    public List<FriendListBean> getFollowList(String username) throws ExecutionException, InterruptedException {
        ExecutorService es = Executors.newSingleThreadExecutor();
        Future<String> future = es.submit(new getResponseData(username,"follow"));
        ResponseBean responseBean = JSON.parseObject(future.get(),ResponseBean.class);
        if (future.isDone()) {

            es.shutdown();
        }
        String data = responseBean.getData().toString().replaceAll("=",":");
        data = data.replaceAll("\"","\'");

        List<FriendListBean> friendListBeanList = JSON.parseObject(data,new TypeReference<List<FriendListBean>>(){});

        return friendListBeanList;
    }
    public List<FriendListBean> getFollowedList(String username) throws ExecutionException, InterruptedException {
        ExecutorService es = Executors.newSingleThreadExecutor();
        Future<String> future = es.submit(new getResponseData(username,"followed"));
        ResponseBean responseBean = JSON.parseObject(future.get(),ResponseBean.class);
        if (future.isDone()) {

            es.shutdown();
        }
        String data = responseBean.getData().toString().replaceAll("=",":");
        data = data.replaceAll("\"","\'");

        List<FriendListBean> friendListBeanList = JSON.parseObject(data,new TypeReference<List<FriendListBean>>(){});

        return friendListBeanList;
    }
    public List<FriendListBean> getArticleList(String username) throws ExecutionException, InterruptedException {
        ExecutorService es = Executors.newSingleThreadExecutor();
        Future<String> future = es.submit(new getResponseData(username,"article"));
        ResponseBean responseBean = JSON.parseObject(future.get(),ResponseBean.class);
        if (future.isDone()) {

            es.shutdown();
        }
        String data = responseBean.getData().toString().replaceAll("=",":");
        data = data.replaceAll("\"","\'");

        List<FriendListBean> friendListBeanList = JSON.parseObject(data,new TypeReference<List<FriendListBean>>(){});

        return friendListBeanList;
    }
    public List<FriendListBean> getCollectionList(String username) throws ExecutionException, InterruptedException {
        ExecutorService es = Executors.newSingleThreadExecutor();
        Future<String> future = es.submit(new getResponseData(username,"collection"));
        ResponseBean responseBean = JSON.parseObject(future.get(),ResponseBean.class);
        if (future.isDone()) {

            es.shutdown();
        }
        String data = responseBean.getData().toString().replaceAll("=",":");
        data = data.replaceAll("\"","\'");

        List<FriendListBean> friendListBeanList = JSON.parseObject(data,new TypeReference<List<FriendListBean>>(){});

        return friendListBeanList;
    }










    class getResponseData implements Callable<String> {

        public getResponseData(String userName, String url) {
            this.userName = userName;
            this.url = url;
        }

        //向后台传的参数
        String userName;

        String url;

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        @Override
        public String call() throws Exception {
            String accessToken = "";
            OkHttpRequest okHttpRequest = new OkHttpRequest();
            String accessTokenUrl = "http://47.101.171.252:8080/user/"+url+"?userName="+userName ;
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
