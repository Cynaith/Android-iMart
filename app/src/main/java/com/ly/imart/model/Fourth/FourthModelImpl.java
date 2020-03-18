package com.ly.imart.model.Fourth;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.ly.imart.bean.Fourth.FourthBean;
import com.ly.imart.bean.Fourth.MyshowBean;
import com.ly.imart.bean.Response.ResponseBean;
import com.ly.imart.model.Login.SignModelImpl;
import com.ly.imart.util.GsonUtil;
import com.ly.imart.util.OkHttpRequest;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FourthModelImpl implements IFourthModel {
    private static final String ACTIVITY_TAG = "FourthModelImpl";

    @Override
    public FourthBean getInfo(String userName) throws ExecutionException, InterruptedException {
        ExecutorService es = Executors.newSingleThreadExecutor();
        Future<String> future = es.submit(new getResponseData(userName));
        ResponseBean responseBean = JSON.parseObject(future.get(),ResponseBean.class);
        if (future.isDone()) {

            es.shutdown();
        }
        String data = responseBean.getData().toString().replaceAll("=",":");
        data = data.replaceAll("\"","\'");

        FourthBean fourthBean = JSON.parseObject(data,FourthBean.class);
        System.out.println(fourthBean.getImgUrl());
        return fourthBean;
    }

    class getResponseData implements Callable<String> {


        public getResponseData(String userName) {
            this.userName = userName;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        //向后台传的参数
        String userName;


        @Override
        public String call() throws Exception {
            String accessToken = "";
            OkHttpRequest okHttpRequest = new OkHttpRequest();
//            String accessTokenUrl = "http://47.101.171.252:8080/user/info?userName="+userName ;
            String accessTokenUrl = "http://47.101.171.252:8080/user/info?userName="+userName ;
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
