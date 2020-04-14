package com.ly.imart.model.Fourth;

import android.util.Log;

import com.ly.imart.maxim.common.utils.SharePreferenceUtils;
import com.ly.imart.util.OkHttpRequest;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class UpdateUserModel {
    private static final String ACTIVITY_TAG = "UpdateUserModel";
    public void UpdateUser(String usershow,String userimg,int age) {
        ExecutorService es = Executors.newSingleThreadExecutor();
        Future<String> future = es.submit(new UpdateUser(usershow, userimg, age));
    }
    class UpdateUser implements Callable<String> {

        String usershow;
        String userimg;
        int age;
        public UpdateUser(String usershow, String userimg, int age) {
            this.usershow = usershow;
            this.userimg = userimg;
            this.age = age;
        }



        @Override
        public String call() throws Exception {
            String accessToken = "";
            OkHttpRequest okHttpRequest = new OkHttpRequest();
            String accessTokenUrl = "http://47.101.171.252:8080/user/update?username="+SharePreferenceUtils.getInstance().getUserName()
                    +"&usershow="+ usershow+"&userimg="+userimg+"&age="+age;
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
