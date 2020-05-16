package com.ly.imart.model.Fourth;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.ly.imart.bean.Fourth.FriendListBean;
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

public class SupportModel {
    public List<FriendListBean> getUserimg() throws ExecutionException, InterruptedException {
        ExecutorService es = Executors.newSingleThreadExecutor();
        Future<String> future = es.submit(new getSupport());
        ResponseBean responseBean = JSON.parseObject(future.get(),ResponseBean.class);
        if (future.isDone()) {

            es.shutdown();
        }
        String data = responseBean.getData().toString().replaceAll("=",":");
        data = data.replaceAll("\"","\'");

        List<FriendListBean> friendListBeanList = JSON.parseObject(data,new TypeReference<List<FriendListBean>>(){});

        return friendListBeanList;
    }
    class getSupport implements Callable<String> {



        @Override
        public String call() throws Exception {
            String accessToken = "";
            OkHttpRequest okHttpRequest = new OkHttpRequest();
            String accessTokenUrl = "http://47.101.171.252:8080/user/support?userName="+ SharePreferenceUtils.getInstance().getUserName();
            try {
                //发送请求
                accessToken = okHttpRequest.get(accessTokenUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return accessToken;
        }
    }
}
