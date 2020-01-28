package com.ly.imart.view.Login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ly.imart.MainActivity;
import com.ly.imart.R;
import com.ly.imart.bean.Login.SignInBean;
import com.ly.imart.presenter.Login.SignInPresenter;

import butterknife.BindFloat;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SignInActivity extends Activity implements ISignInView, View.OnClickListener {


    @BindView(R.id.SignIn_inputPhone)
    EditText editText_inputPhone;

    @BindView(R.id.SignIn_inputCode)
    EditText editText_inputCode;

    @BindView(R.id.SignIn_getCodeButton)
    Button button_getCode;

    @BindView(R.id.SignIn_signInButton)
    Button button_signIn;

    SignInPresenter signInPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        signInPresenter = new SignInPresenter(this);
        setContentView(R.layout.activity_signin);
        ButterKnife.bind(this);
        button_signIn.setOnClickListener(this);
        button_getCode.setOnClickListener(this);
    }




    @Override
    public Long getId() {
        return null;
    }

    @Override
    public Long getPhone() {

        return Long.parseLong(editText_inputPhone.getText().toString());
    }

    @Override
    public Long getVerificationCode() {

        return Long.parseLong(editText_inputCode.getText().toString());
    }

    @Override
    public void setPhone(String userPhone) {
        editText_inputPhone.setText(userPhone);
    }

    @Override
    public void setVerificationCode(String verificationCode) {
        editText_inputCode.setText(verificationCode);
    }

    @Override
    public void signIn() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void register() {
        startActivity(new Intent(this,PersonalDataActivity.class));
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.SignIn_getCodeButton:
                signInPresenter.sendCode(getPhone());
                Toast.makeText(getApplicationContext(),"点击了",Toast.LENGTH_SHORT).show();
                break;
            case R.id.SignIn_signInButton:
                System.out.println(getPhone()+"  "+getVerificationCode());
                signInPresenter.setSignInBean(getPhone(),getVerificationCode());
                signInPresenter.checkSignIn();
                Toast.makeText(getApplicationContext(),"点击了",Toast.LENGTH_SHORT).show();
                break;
        }
    }

}
