package com.ly.imart.model.Third;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.ly.imart.bean.Response.ResponseBean;
import com.ly.imart.bean.Third.VideoBean;
import com.ly.imart.maxim.common.utils.SharePreferenceUtils;
import com.ly.imart.model.Others.AddArticleModel;
import com.ly.imart.util.OkHttpRequest;

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

public class VideoModel {
    private static final String ACTIVITY_TAG = "VideoModel";


    public List<VideoBean> getInfo() throws ExecutionException, InterruptedException {
        ExecutorService es = Executors.newSingleThreadExecutor();
        Future<String> future = es.submit(new getResponseData());
        ResponseBean responseBean = JSON.parseObject(future.get(),ResponseBean.class);
        if (future.isDone()) {

            es.shutdown();
        }
        String data = responseBean.getData().toString().replaceAll("=",":");
        //data = data.replaceAll("\"","\'");

        List<VideoBean> videoBeans = JSON.parseObject(data,new TypeReference<List<VideoBean>>(){});
        return videoBeans;
    }

    class getResponseData implements Callable<String> {



        @Override
        public String call() throws Exception {
            String accessToken = "";
            OkHttpRequest okHttpRequest = new OkHttpRequest();

            String accessTokenUrl = "http://10.0.2.2:8080/video/getVideo?username="+SharePreferenceUtils.getInstance().getUserName() ;
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



    public String uploadVideo(String videoPath) throws ExecutionException, InterruptedException {
        ExecutorService es = Executors.newSingleThreadExecutor();
        Future<String> future = es.submit(new uploadVideo(videoPath));
        String videourl = future.get();
        if (future.isDone()) {
            es.shutdown();
        }
        Log.d(ACTIVITY_TAG,videourl);
        return videourl;
    }
    class uploadVideo implements Callable<String> {

        String videoPath;

        public uploadVideo(String videoPath) {
            this.videoPath = videoPath;
        }

        @Override
        public String call() throws Exception {
            OkHttpClient okHttpClient = new OkHttpClient();
            String url = "http://47.101.171.252:8890/uploadFile?username="+ SharePreferenceUtils.getInstance().getUserName()+"上传的视频";
            Log.d("videopath", videoPath);
            File file = new File(videoPath);
            RequestBody image = RequestBody.create(MediaType.parse("video/*"), file);
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", videoPath, image)
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
    public boolean addVideo(String videoUrl,String title) throws ExecutionException, InterruptedException {
        ExecutorService es = Executors.newSingleThreadExecutor();
        Future<String> future = es.submit(new addVideo(title,videoUrl));
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

    class addVideo implements Callable<String> {

        String title;
        String videoUrl;

        public addVideo(String title, String videoUrl) {
            this.title = title;
            this.videoUrl = videoUrl;
        }

        @Override
        public String call() throws Exception {
            String accessToken = "";
            OkHttpRequest okHttpRequest = new OkHttpRequest();

            String accessTokenUrl = "http://10.0.2.2:8080/video/addvideo?username="+SharePreferenceUtils.getInstance().getUserName()
                    +"&title="+title+"&videourl="+videoUrl ;
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
    public boolean support(int videoid) throws ExecutionException, InterruptedException {
        ExecutorService es = Executors.newSingleThreadExecutor();
        Future<String> future = es.submit(new support(videoid));
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

    class support implements Callable<String> {

       int videoid;

        public support(int videoid) {
            this.videoid = videoid;
        }

        @Override
        public String call() throws Exception {
            String accessToken = "";
            OkHttpRequest okHttpRequest = new OkHttpRequest();

            String accessTokenUrl = "http://10.0.2.2:8080/video/supportvideo?videoid="+videoid+"&username="+SharePreferenceUtils.getInstance().getUserName();
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
