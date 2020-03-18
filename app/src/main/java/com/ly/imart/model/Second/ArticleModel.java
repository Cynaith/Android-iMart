package com.ly.imart.model.Second;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.ly.imart.bean.Fourth.MyshowFragmentBean;
import com.ly.imart.bean.Response.ResponseBean;
import com.ly.imart.bean.Second.ArticleBean;
import com.ly.imart.model.Fourth.FourthMyshowFragmentModel;
import com.ly.imart.util.OkHttpRequest;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ArticleModel {
    private static final String ACTIVITY_TAG = "ArticleModel";


    public ArticleBean getArticle(int articleid) throws ExecutionException, InterruptedException {
        ExecutorService es = Executors.newSingleThreadExecutor();
        Future<String> future = es.submit(new getResponseData(articleid));
        ResponseBean responseBean = JSON.parseObject(future.get(),ResponseBean.class);
        if (future.isDone()) {
            es.shutdown();
        }
        System.out.println(responseBean.getData().toString());
        String data = responseBean.getData().toString().replaceAll("=",":");
        data = data.replaceAll("\"","\'");
        ArticleBean articleBean = JSON.parseObject(data,ArticleBean.class);
        return articleBean;
    }



    class getResponseData implements Callable<String> {

       int id;

        public getResponseData(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        @Override
        public String call() throws Exception {
            String accessToken = "";
            OkHttpRequest okHttpRequest = new OkHttpRequest();
            String accessTokenUrl = "http://47.101.171.252:8080/article/getById?id="+id ;
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
