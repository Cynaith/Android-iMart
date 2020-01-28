package com.ly.imart.bean.Login;

public class SignInBean {

    private Long id;
    private Long phone;
    private Long VerificationCode;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPhone() {
        return phone;
    }

    public void setPhone(Long phone) {
        this.phone = phone;
    }

    public Long getVerificationCode() {
        return VerificationCode;
    }

    public void setVerificationCode(Long verificationCode) {
        VerificationCode = verificationCode;
    }
}
