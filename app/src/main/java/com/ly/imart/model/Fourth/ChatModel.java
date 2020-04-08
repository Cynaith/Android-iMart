package com.ly.imart.model.Fourth;

import android.util.Log;

import com.ly.imart.maxim.common.utils.SharePreferenceUtils;
import com.ly.imart.model.Third.VideoModel;
import com.ly.imart.util.OkHttpRequest;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatModel {
    public String getUserimg(String username) throws ExecutionException, InterruptedException {
        ExecutorService es = Executors.newSingleThreadExecutor();
        Future<String> future = es.submit(new getUserimg(username));
        String imgurl = future.get();
        if (future.isDone()) {
            es.shutdown();
        }

        return imgurl;
    }
    class getUserimg implements Callable<String> {

        String username;

        public getUserimg(String username) {
            this.username = username;
        }

        @Override
        public String call() throws Exception {
            String accessToken = "";
            OkHttpRequest okHttpRequest = new OkHttpRequest();
            String accessTokenUrl = "http://47.101.171.252:8080/user/getUserimg?username="+username ;
            try {
                //发送请求
                accessToken = okHttpRequest.get(accessTokenUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(""+username+accessToken);
            return accessToken;
        }
    }
}
