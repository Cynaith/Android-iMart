package com.ly.imart.view.Login;

public interface ISignInView {
    Long getId();
    Long getPhone();
    Long getVerificationCode();
    void setPhone(String userPhone);
    void setVerificationCode(String verificationCode);
    void signIn();
    void register();
}



