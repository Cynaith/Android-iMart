package com.ly.imart.model.Second;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.ly.imart.bean.Response.ResponseBean;
import com.ly.imart.bean.Second.ActionStatusBean;
import com.ly.imart.bean.Second.ArticleBean;
import com.ly.imart.bean.Second.ReplyDetailBean;
import com.ly.imart.maxim.common.utils.SharePreferenceUtils;
import com.ly.imart.util.OkHttpRequest;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ArticleMiddleModel {
    private static final String ACTIVITY_TAG = "ArticleMiddleModel";


    public void action(int articleId,int kind) throws ExecutionException, InterruptedException {
        ExecutorService es = Executors.newSingleThreadExecutor();
        Future<String> future = es.submit(new getResponseData(articleId,kind));
        future.get();
    }



    class getResponseData implements Callable<String> {

       int articleId;
       int kind; //点赞1 收藏2

        public getResponseData(int articleId, int kind) {
            this.articleId = articleId;
            this.kind = kind;
        }

        public int getArticleId() {
            return articleId;
        }

        public void setArticleId(int articleId) {
            this.articleId = articleId;
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
            String accessTokenUrl = "http://47.101.171.252:8080/article/action?username="+
                    SharePreferenceUtils.getInstance().getUserName()+
                    "&articleId="+articleId+"&kind="+kind;
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

    public ActionStatusBean actionStatus(int articleId) throws ExecutionException, InterruptedException {
        ExecutorService es = Executors.newSingleThreadExecutor();
        Future<String> future = es.submit(new getAction(articleId));
        ResponseBean responseBean = JSON.parseObject(future.get(),ResponseBean.class); //这个ResponseBean和后端的ResponseWrapper都抄过来就好
        if (future.isDone()) {

            es.shutdown();
        }
        String data = responseBean.getData().toString().replaceAll("=",":");
//        data = data.replaceAll("\"","\'"); //这里如果解析出问题，可以加上
        System.out.println(data);

        ActionStatusBean actionStatusBean = JSON.parseObject(data,ActionStatusBean.class);//后台传过来的数据解析到Bean里
        return actionStatusBean;
    }



    class getAction implements Callable<String> {

        int articleId;


        public getAction(int articleId) {
            this.articleId = articleId;
        }


        @Override
        public String call() throws Exception {
            String accessToken = "";
            OkHttpRequest okHttpRequest = new OkHttpRequest();
            String accessTokenUrl = "http://47.101.171.252:8080/article/actionStatus?username="+
                    SharePreferenceUtils.getInstance().getUserName()+
                    "&articleId="+articleId;
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
