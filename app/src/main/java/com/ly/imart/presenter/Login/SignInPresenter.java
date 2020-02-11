package com.ly.imart.presenter.Login;

import android.util.Log;

import com.ly.imart.bean.Login.SignInBean;
import com.ly.imart.model.Login.ISignInModel;
import com.ly.imart.model.Login.SignModelImpl;
import com.ly.imart.view.Login.ISignInView;

public class SignInPresenter {
    private static final String ACTIVITY_TAG = "SignInPresenter";

    private SignInBean signInBean = new SignInBean();
    private ISignInView iSignInView;
    private ISignInModel iSignInModel;

    public SignInPresenter(ISignInView iSignInView) {
        this.iSignInView = iSignInView;
        iSignInModel = new SignModelImpl();
    }

    public boolean setSignInBean(Long userphone,Long code){
        signInBean.setPhone(userphone);
        signInBean.setVerificationCode(code);
        return true;
    }


    public boolean sendCode(Long userphone){
        return true;
    }

    public boolean checkSignIn() throws Exception{
        String responseCode = iSignInModel.checkSignIn(signInBean.getPhone(),signInBean.getVerificationCode());
        if(responseCode.equals("0")){
            Log.d("账号验证","通过");
            iSignInView.signIn();
            return true;
        }
        else if (responseCode.equals("0001")){
            Log.d("账号验证","通过未注册");
            iSignInView.register();
            return false;
        }
        else{
            return false;
        }
    }



}
