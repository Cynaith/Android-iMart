package com.ly.imart.view.Fourth;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ly.imart.R;
import com.ly.imart.maxim.common.utils.SharePreferenceUtils;
import com.ly.imart.model.Fourth.UpdateUserModel;
import com.ly.imart.model.Others.AddArticleModel;
import com.ly.imart.util.MyImageView;
import com.ly.imart.view.Others.AddArticle;

import java.io.File;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UpdateUserActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.user_name_update)
    TextView editText_username;
    @BindView(R.id.user_show_update)
    EditText editText_usershow;
    @BindView(R.id.user_age_update)
    EditText editText_age;
    @BindView(R.id.user_img_update)
    MyImageView userimg;
    @BindView(R.id.user_update_button)
    TextView next_button;

    private static final int REQUEST_CODE_PICK_IMAGE = 1023;
    private String imgPath;
    private boolean isUpload=false;
    private UpdateUserModel updateUserModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
        ButterKnife.bind(this);
        editText_username.setText(SharePreferenceUtils.getInstance().getUserName());
        userimg.setOnClickListener(this);
        next_button.setClickable(true);
        next_button.setOnClickListener(this);
        updateUserModel = new UpdateUserModel();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.user_img_update:
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");// 相片类型
                startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
                break;
            case R.id.user_update_button:
                nextPage();
                break;
        }
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
    private void nextPage(){
        String imgUrl = "";
        if (isUpload) {
            AddArticleModel addArticleModel = new AddArticleModel();
            try {
                imgUrl = addArticleModel.uploadImg(imgPath);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        String usershow = editText_usershow.getText().toString();
        int userage = 0;
        if (editText_age.getText().toString().equals(""))
            userage = 0;
         else {
             try{
                 userage = Integer.parseInt(editText_age.getText().toString());
             }catch (Exception e){
                 Toast.makeText(this, "年龄必须为数字", Toast.LENGTH_SHORT).show();
                 return;
             }
             
        }
        updateUserModel.UpdateUser(usershow,imgUrl,userage);
        Toast.makeText(this,"修改成功",Toast.LENGTH_SHORT).show();
        finish();
    }
}
