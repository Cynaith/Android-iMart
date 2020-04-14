package com.ly.imart.view.Login;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ly.imart.maxim.login.view.LoginActivity;
import com.ly.imart.R;
import com.ly.imart.model.Fourth.UpdateUserModel;
import com.ly.imart.model.Login.PersonalDataModel;
import com.ly.imart.model.Others.AddArticleModel;
import com.ly.imart.util.MyImageView;

import java.io.File;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PersonalDataActivity extends Activity implements View.OnClickListener {

    @BindView(R.id.PersonalData_updateimg)
    MyImageView userimg;

    @BindView(R.id.PersonalData_inputUsershow)
    EditText editText_inputUsershow;


    @BindView(R.id.PersonalData_inputAge)
    EditText editText_inputAge;

    @BindView(R.id.PersonalData_commitButton)
    TextView button_commit;

    private static final int REQUEST_CODE_PICK_IMAGE = 1023;
    private String imgPath;
    private boolean isUpload=false;
    private UpdateUserModel updateUserModel;
    String userName;
    String userPwd;
    boolean isLoginById;
    String mAppId;
    String phone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personaldata);
        ButterKnife.bind(this);
        initView();
        getData();
    }

    void initView(){
        button_commit.setClickable(true);
        button_commit.setOnClickListener(this);
        userimg.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.PersonalData_commitButton:
                Toast.makeText(getApplicationContext(),"点击了进入i-Mart",Toast.LENGTH_SHORT).show();
                goHome();
                break;
            case R.id.PersonalData_updateimg:
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");// 相片类型
                startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
                break;
        }
    }

    void getData(){
        Intent intent = getIntent();
        userName = intent.getStringExtra("userName");
        userPwd = intent.getStringExtra("userPwd");
        isLoginById = intent.getBooleanExtra("isLoginById",false);
        mAppId = intent.getStringExtra("AppId");
        phone = intent.getStringExtra("phone");

    }


    public void goHome() {
//        startActivity(new Intent(this, MainActivity.class));
//        LoginActivity.login(this,userName,userPwd,isLoginById,mAppId);

//        finish();
        String imgUrl = null;
        if (isUpload) {
            AddArticleModel addArticleModel = new AddArticleModel();
            try {
                imgUrl = addArticleModel.uploadImg(imgPath);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else {
            Toast.makeText(this,"请上传头像",Toast.LENGTH_SHORT).show();
            return;
        }
        String usershow = editText_inputUsershow.getText().toString();
        String age = editText_inputAge.getText().toString();
        int userage = 0;
        if (usershow.equals("")||usershow==null||age.equals("")||age==null){
            Toast.makeText(this,"请填写用户简介",Toast.LENGTH_SHORT).show();
            return;
        }
        try{
            userage = Integer.parseInt(age);
        }catch (Exception e){
            Toast.makeText(this, "年龄必须为数字", Toast.LENGTH_SHORT).show();
            return;
        }
        PersonalDataModel personalDataModel = new PersonalDataModel();
        personalDataModel.UpdateUser(userName,userPwd,usershow,imgUrl,phone,userage);
        LoginActivity.login(this, userName, userPwd, false, mAppId);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_PICK_IMAGE) {
            Uri uri = data.getData();
            insertBitmap(getRealFilePath(uri));
        }
    }
    private void insertBitmap(String imagePath) {
        File file = new File(imagePath);
        Uri uri = Uri.fromFile(file);
        userimg.setImageURI(uri);
        imgPath = imagePath;
        isUpload = true;
    }
    /**
     * 根据Uri获取图片文件的绝对路径
     */
    public String getRealFilePath(final Uri uri) {
        if (null == uri) {
            return null;
        }

        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = getContentResolver().query(uri,
                    new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }
}
