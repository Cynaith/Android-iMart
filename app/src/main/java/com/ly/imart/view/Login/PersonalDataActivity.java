package com.ly.imart.view.Login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ly.imart.maxim.login.view.LoginActivity;
import com.ly.imart.view.MainActivity;
import com.ly.imart.R;
import com.ly.imart.presenter.Login.PersonalDataPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PersonalDataActivity extends Activity implements View.OnClickListener,IPersonalDataView {

    @BindView(R.id.PersonalData_inputName)
    EditText editText_inputName;

    @BindView(R.id.PersonalData_inputMail)
    EditText editText_inputMail;

    @BindView(R.id.PersonalData_inputAge)
    EditText editText_inputAge;

    @BindView(R.id.PersonalData_commitButton)
    Button button_commit;

    PersonalDataPresenter personalDataPresenter;

    String userName;
    String userPwd;
    boolean isLoginById;
    String mAppId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personaldata);
        personalDataPresenter = new PersonalDataPresenter(this);
        ButterKnife.bind(this);
        button_commit.setOnClickListener(this);
        getData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.PersonalData_commitButton:
                Toast.makeText(getApplicationContext(),"点击了进入i-Mart",Toast.LENGTH_SHORT).show();
                personalDataPresenter.commit();
                goHome();
                break;
        }
    }

    void getData(){
        Intent intent = getIntent();
        userName = intent.getStringExtra("userName");
        userPwd = intent.getStringExtra("userPwd");
        isLoginById = intent.getBooleanExtra("isLoginById",false);
        mAppId = intent.getStringExtra("AppId");

    }

    @Override
    public String getName() {

        return editText_inputName.getText().toString();
    }

    @Override
    public String getMail() {
        return editText_inputMail.getText().toString();
    }

    @Override
    public int getAge() {
        return Integer.parseInt(editText_inputAge.getText().toString());
    }

    @Override
    public void setName() {

    }

    @Override
    public void setMail() {

    }

    @Override
    public void setAge() {

    }

    @Override
    public void goHome() {
//        startActivity(new Intent(this, MainActivity.class));
//        LoginActivity.login(this,userName,userPwd,isLoginById,mAppId);
        LoginActivity.login(this, userName, userPwd, false, mAppId);
//        finish();
    }
}
