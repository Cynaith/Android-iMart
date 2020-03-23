package com.ly.imart.model.Second;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.ly.imart.bean.Fourth.FourthBean;
import com.ly.imart.bean.Fourth.MyshowFragmentBean;
import com.ly.imart.bean.Response.ResponseBean;
import com.ly.imart.bean.Second.ArticleMainBean;
import com.ly.imart.bean.Second.CommentDetailBean;
import com.ly.imart.bean.Second.ReplyDetailBean;
import com.ly.imart.maxim.common.utils.SharePreferenceUtils;
import com.ly.imart.model.Fourth.FourthModelImpl;
import com.ly.imart.util.OkHttpRequest;
import com.ly.imart.util.SystemInfo;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ArticleMainModelImpl implements IArticleMainModel {

    private static final String ACTIVITY_TAG = "ArticleMainModelImpl";


    // 参数是前端传来准备发到后端的数据
    public List<CommentDetailBean> getComment(int articleId) throws ExecutionException, InterruptedException {
        ExecutorService es = Executors.newSingleThreadExecutor();
        Future<String> future = es.submit(new getResponseData(articleId));
        ResponseBean responseBean = JSON.parseObject(future.get(),ResponseBean.class); //这个ResponseBean和后端的ResponseWrapper都抄过来就好
        if (future.isDone()) {

            es.shutdown();
        }
        String data = responseBean.getData().toString().replaceAll("=",":");
//        data = data.replaceAll("\"","\'"); //这里如果解析出问题，可以加上
        System.out.println(data);

        List<CommentDetailBean> commentDetailBeanList = JSON.parseObject(data,new TypeReference<List<CommentDetailBean>>(){});//后台传过来的数据解析到Bean里
        return commentDetailBeanList;
    }

    class getResponseData implements Callable<String> {
//下面只要改两个地方
// articleId换成你要传的参数，然后生成全参数的构造函数就行
// 接口地址 accessTokenUrl

       int articleId;

        public getResponseData(int articleId) {
            this.articleId = articleId;
        }

        public int getArticleId() {
            return articleId;
        }

        public void setArticleId(int articleId) {
            this.articleId = articleId;
        }

        @Override
        public String call() throws Exception {
            String accessToken = "";
            OkHttpRequest okHttpRequest = new OkHttpRequest();
//            String accessTokenUrl = "http://47.101.171.252:8080/user/info?userName="+userName ;
            String accessTokenUrl = "http://47.101.171.252:8080/article/getComment?articleId="+articleId ;
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

    public CommentDetailBean postComment(int articleId,String commentContent) throws ExecutionException, InterruptedException {
        ExecutorService es = Executors.newSingleThreadExecutor();
        Future<String> future = es.submit(new postResponseData(articleId,commentContent));
        ResponseBean responseBean = JSON.parseObject(future.get(),ResponseBean.class); //这个ResponseBean和后端的ResponseWrapper都抄过来就好
        if (future.isDone()) {

            es.shutdown();
        }
        String data = responseBean.getData().toString().replaceAll("=",":");
//        data = data.replaceAll("\"","\'"); //这里如果解析出问题，可以加上
        System.out.println(data);

        CommentDetailBean commentDetailBean = JSON.parseObject(data,CommentDetailBean.class);//后台传过来的数据解析到Bean里
        return commentDetailBean;
    }

    class postResponseData implements Callable<String> {
//下面只要改两个地方
// articleId换成你要传的参数，然后生成全参数的构造函数就行
// 接口地址 accessTokenUrl

        int articleId;
        String commentContent;

        public postResponseData(int articleId, String commentContent) {
            this.articleId = articleId;
            this.commentContent = commentContent;
        }

        @Override
        public String call() throws Exception {
            String accessToken = "";
            OkHttpRequest okHttpRequest = new OkHttpRequest();
//            String accessTokenUrl = "http://47.101.171.252:8080/user/info?userName="+userName ;
            String accessTokenUrl = "http://47.101.171.252:8080/article/postComment?articleId="+articleId
                    +"&username="+ SharePreferenceUtils.getInstance().getUserName()+"&content="+commentContent;
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
    public ReplyDetailBean postReply(int commentId, String content) throws ExecutionException, InterruptedException {
        ExecutorService es = Executors.newSingleThreadExecutor();
        Future<String> future = es.submit(new postReply(commentId,content));
        ResponseBean responseBean = JSON.parseObject(future.get(),ResponseBean.class); //这个ResponseBean和后端的ResponseWrapper都抄过来就好
        if (future.isDone()) {

            es.shutdown();
        }
        String data = responseBean.getData().toString().replaceAll("=",":");
//        data = data.replaceAll("\"","\'"); //这里如果解析出问题，可以加上
        System.out.println(data);

        ReplyDetailBean replyDetailBean = JSON.parseObject(data,ReplyDetailBean.class);//后台传过来的数据解析到Bean里
        return replyDetailBean;
    }

    class postReply implements Callable<String> {

        int commentId;
        String content;

        public postReply(int commentId, String content) {
            this.commentId = commentId;
            this.content = content;
        }

        @Override
        public String call() throws Exception {
            String accessToken = "";
            OkHttpRequest okHttpRequest = new OkHttpRequest();
//            String accessTokenUrl = "http://47.101.171.252:8080/user/info?userName="+userName ;
            String accessTokenUrl = "http://47.101.171.252:8080/article/postReply?commentId="+commentId
                    +"&username="+ SharePreferenceUtils.getInstance().getUserName()+"&content="+content;
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
