package com.ly.imart.model.Login;

import com.ly.imart.bean.Login.SignInBean;

public interface ISignInModel {

    String checkSignIn(Long userPhone,Long Code) throws Exception;

}
