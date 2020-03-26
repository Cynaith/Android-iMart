package com.ly.imart.model.Fourth;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.ly.imart.bean.Fourth.DiyListBean;
import com.ly.imart.bean.Fourth.FourthBean;
import com.ly.imart.bean.Fourth.FriendListBean;
import com.ly.imart.bean.Response.ResponseBean;
import com.ly.imart.util.OkHttpRequest;
import com.ly.imart.util.ipUtils;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class DiyListModel {
    private static final String ACTIVITY_TAG = "DiyListModel";


    public List<DiyListBean> getInfo(double lat,double lng) throws ExecutionException, InterruptedException {
        ExecutorService es = Executors.newSingleThreadExecutor();
        Future<String> future = es.submit(new getResponseData(lat, lng));
        ResponseBean responseBean = JSON.parseObject(future.get(),ResponseBean.class);
        if (future.isDone()) {

            es.shutdown();
        }
        String data = responseBean.getData().toString().replaceAll("=",":");
        data = data.replaceAll("\"","\'");

        List<DiyListBean> diyListBean = JSON.parseObject(data,new TypeReference<List<DiyListBean>>(){});
        return diyListBean;
    }

    class getResponseData implements Callable<String> {


        double lat;
        double lng;

        public getResponseData(double lat, double lng) {
            this.lat = lat;
            this.lng = lng;
        }

        @Override
        public String call() throws Exception {
            String accessToken = "";
            OkHttpRequest okHttpRequest = new OkHttpRequest();

            String accessTokenUrl = "http://10.0.2.2:8080/map/getDiyByLocation?lat="+lat+"&lng="+lng ;
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
