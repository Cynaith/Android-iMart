package com.ly.imart.model.Second;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.ly.imart.bean.Response.ResponseBean;
import com.ly.imart.bean.Second.ArticleBean;
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
            String accessTokenUrl = "http://10.0.2.2:8080/article/action?username="+
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
}
