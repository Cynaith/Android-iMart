package com.ly.imart.model.Login;

import android.util.Log;

import com.ly.imart.maxim.common.utils.SharePreferenceUtils;
import com.ly.imart.model.Fourth.UpdateUserModel;
import com.ly.imart.util.OkHttpRequest;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class PersonalDataModel{

    private static final String ACTIVITY_TAG = "UpdateUserModel";
    public void UpdateUser(String username, String password, String usershow, String userimg, String phone,int age) {
        ExecutorService es = Executors.newSingleThreadExecutor();
        Future<String> future = es.submit(new UpdateUser(username, password, usershow, userimg, age,phone));
    }
    class UpdateUser implements Callable<String> {

        String username;
        String password;
        String usershow;
        String userimg;
        String phone;
        int age;

        public UpdateUser(String username, String password, String usershow, String userimg, int age,String phone) {
            this.username = username;
            this.password = password;
            this.usershow = usershow;
            this.userimg = userimg;
            this.phone = phone;
            this.age = age;
        }

        @Override
        public String call() throws Exception {
            String accessToken = "";
            OkHttpRequest okHttpRequest = new OkHttpRequest();
            String accessTokenUrl = "http://10.0.2.2:8080/user/register?username="+ username
                    +"&usershow="+ usershow+"&imgurl="+userimg+"&age="+age+"&phone="+phone+"&password="+password;
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
