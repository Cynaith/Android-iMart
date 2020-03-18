package com.ly.imart.model.Second;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.ly.imart.bean.Fourth.MyshowFragmentBean;
import com.ly.imart.bean.Response.ResponseBean;
import com.ly.imart.bean.Second.SecondFragmentBean;
import com.ly.imart.model.Fourth.FourthMyshowFragmentModel;
import com.ly.imart.util.OkHttpRequest;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SecondFragmentViewPageModel {

    private static final String ACTIVITY_TAG = "SecondViewPageModel";


    public List<SecondFragmentBean> getKindArticle(int kind) throws ExecutionException, InterruptedException {
        ExecutorService es = Executors.newSingleThreadExecutor();
        Future<String> future = es.submit(new getResponseData(kind));
        ResponseBean responseBean = JSON.parseObject(future.get(),ResponseBean.class);
        if (future.isDone()) {
            es.shutdown();
        }
        String data = responseBean.getData().toString().replaceAll("=",":");
        data = data.replaceAll("\"","\'");
        List<SecondFragmentBean> secondFragmentBeans = JSON.parseObject(data,new TypeReference<List<SecondFragmentBean>>(){});
        return secondFragmentBeans;
    }




    class getResponseData implements Callable<String> {

        int kind;

        public getResponseData(int kind) {
            this.kind = kind;
        }

        public int getKind() {
            return kind;
        }

        public void setKind(int kind) {
            this.kind = kind;
        }

        @Override
        public String call() throws Exception {
            String accessToken = "";
            OkHttpRequest okHttpRequest = new OkHttpRequest();
            String accessTokenUrl = "http://47.101.171.252:8080/article/getAll?kind="+kind ;
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
