package com.ly.imart.model.Others;

import android.util.Log;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.ly.imart.bean.Fourth.FriendListBean;
import com.ly.imart.bean.Response.ResponseBean;
import com.ly.imart.maxim.common.utils.SharePreferenceUtils;
import com.ly.imart.model.Fourth.FollowListModelImpl;
import com.ly.imart.util.OkHttpRequest;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;
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

public class AddArticleModel {
    private static final String ACTIVITY_TAG = "AddArticleModel";

    public String uploadImg(String imagePath) throws ExecutionException, InterruptedException {
        ExecutorService es = Executors.newSingleThreadExecutor();
        Future<String> future = es.submit(new getResponseData(imagePath));
        String imgUrl = future.get();
        if (future.isDone()) {
            es.shutdown();
        }
        Log.d(ACTIVITY_TAG,imgUrl);
        return imgUrl;
    }
    public boolean postArticle(String title,String text,int kind,String img1) throws ExecutionException, InterruptedException {
        ExecutorService es = Executors.newSingleThreadExecutor();
        Future<String> future = es.submit(new postArticle(SharePreferenceUtils.getInstance().getUserName(),title,text,kind,img1));
        ResponseBean responseBean = JSON.parseObject(future.get(),ResponseBean.class);
        if (future.isDone()) {
            es.shutdown();
        }
        String data = responseBean.getData().toString();
        Log.d(ACTIVITY_TAG,data);
        if (data == "true"){
            return true;
        }
        else return false;
    }
    class getResponseData implements Callable<String> {

        String imagePath;

        public getResponseData(String imagePath) {
            this.imagePath = imagePath;
        }

        public String getImagePath() {
            return imagePath;
        }

        public void setImagePath(String imagePath) {
            this.imagePath = imagePath;
        }

        @Override
        public String call() throws Exception {
            OkHttpClient okHttpClient = new OkHttpClient();
            String url = "http://47.101.171.252:8890/uploadFile?username="+ SharePreferenceUtils.getInstance().getUserName();
//            String url = "http://47.101.171.252:8890/uploadFile?username=jack";
            Log.d("imagePath", imagePath);
            File file = new File(imagePath);
            RequestBody image = RequestBody.create(MediaType.parse("image/*"), file);
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", imagePath, image)
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            Log.d(ACTIVITY_TAG,response.body().toString());
            return response.body().string();
        }
    }
    class postArticle implements Callable<String> {

        String username;
        String title;
        String text;
        int kind;
        String img1;

        public postArticle(String username, String title, String text, int kind, String img1) {
            this.username = username;
            this.title = title;
            this.text = text;
            this.kind = kind;
            this.img1 = img1;
        }

        public String getImg1() {
            return img1;
        }

        public void setImg1(String img1) {
            this.img1 = img1;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
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
            String accessTokenUrl = "http://47.101.171.252:8080/article/add?username="+username+
                    "&title="+title+"&text="+text+"&kind="+kind+"&img1="+img1 ;
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
