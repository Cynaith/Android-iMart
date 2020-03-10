package com.ly.imart.model.Fourth;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.ly.imart.bean.Fourth.FriendListBean;
import com.ly.imart.bean.Fourth.MyshowFragmentBean;
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

public class FourthMyshowFragmentModel {

    private static final String ACTIVITY_TAG = "FourthMyshowFragmentModel";


    public List<MyshowFragmentBean> getMyshow1(String username) throws ExecutionException, InterruptedException {
        ExecutorService es = Executors.newSingleThreadExecutor();
        Future<String> future = es.submit(new getResponseData(username,"myshow1"));
        ResponseBean responseBean = JSON.parseObject(future.get(),ResponseBean.class);
        if (future.isDone()) {
            es.shutdown();
        }
        String data = responseBean.getData().toString().replaceAll("=",":");
        data = data.replaceAll("\"","\'");
        List<MyshowFragmentBean> myshowFragmentBeans = JSON.parseObject(data,new TypeReference<List<MyshowFragmentBean>>(){});
        return myshowFragmentBeans;
    }
    public List<MyshowFragmentBean> getMyshow2(String username) throws ExecutionException, InterruptedException {
        ExecutorService es = Executors.newSingleThreadExecutor();
        Future<String> future = es.submit(new getResponseData(username,"myshow1"));
        ResponseBean responseBean = JSON.parseObject(future.get(),ResponseBean.class);
        if (future.isDone()) {

            es.shutdown();
        }
        String data = responseBean.getData().toString().replaceAll("=",":");
        data = data.replaceAll("\"","\'");

        List<MyshowFragmentBean> myshowFragmentBeans = JSON.parseObject(data,new TypeReference<List<MyshowFragmentBean>>(){});

        return myshowFragmentBeans;
    }
    public List<MyshowFragmentBean> getMyshow3(String username) throws ExecutionException, InterruptedException {
        ExecutorService es = Executors.newSingleThreadExecutor();
        Future<String> future = es.submit(new getResponseData(username,"myshow1"));
        ResponseBean responseBean = JSON.parseObject(future.get(),ResponseBean.class);
        if (future.isDone()) {

            es.shutdown();
        }
        String data = responseBean.getData().toString().replaceAll("=",":");
        data = data.replaceAll("\"","\'");

        List<MyshowFragmentBean> myshowFragmentBeans = JSON.parseObject(data,new TypeReference<List<MyshowFragmentBean>>(){});

        return myshowFragmentBeans;
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
            String accessTokenUrl = "http://10.0.2.2:8080/user/"+url+"?userName="+userName ;
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
