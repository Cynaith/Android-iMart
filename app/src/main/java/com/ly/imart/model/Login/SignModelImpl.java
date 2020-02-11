package com.ly.imart.model.Login;

import android.util.Log;

import com.ly.imart.bean.Response.ResponseBean;
import com.ly.imart.util.GsonUtil;
import com.ly.imart.util.OkHttpRequest;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SignModelImpl implements ISignInModel {
    private static final String ACTIVITY_TAG = "SignModelImpl";

    @Override
    public String checkSignIn(Long userPhone, Long code) throws Exception{

        ExecutorService es = Executors.newSingleThreadExecutor();
        Future<String> future = es.submit(new getResponseData(userPhone, code));

        ResponseBean responseBean = GsonUtil.parseJsonWithGson(future.get(), ResponseBean.class);
            Log.d(ACTIVITY_TAG, "用户登录->后台响应代码:" + responseBean.getCode());
        if (future.isDone()) {

            es.shutdown();
        }

        return responseBean.getCode();
    }

    class getResponseData implements Callable<String> {


        Long userPhone;
        Long code;

        public Long getUserPhone() {
            return userPhone;
        }

        public void setUserPhone(Long userPhone) {
            this.userPhone = userPhone;
        }

        public Long getCode() {
            return code;
        }

        public void setCode(Long code) {
            this.code = code;
        }

        public getResponseData(Long userPhone, Long code) {
            this.userPhone = userPhone;
            this.code = code;
        }

        @Override
        public String call() throws Exception {
            String accessToken = "";
            OkHttpRequest okHttpRequest = new OkHttpRequest();
            String accessTokenUrl = "http://10.0.2.2:8900/user/login?userPhone=" + userPhone + "&userCode=" + code;
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
