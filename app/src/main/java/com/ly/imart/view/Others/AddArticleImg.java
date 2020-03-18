package com.ly.imart.view.Others;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ly.imart.R;
import com.ly.imart.model.Others.AddArticleModel;
import com.ly.imart.model.Second.ArticleModel;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

@SuppressLint("SimpleDateFormat")
public class AddArticleImg extends FragmentActivity implements View.OnClickListener {
    private static final int REQUEST_CODE_PICK_IMAGE = 1023;
    private static final int REQUEST_CODE_CAPTURE_CAMEIA = 1022;
    private static final File PHOTO_DIR = new File(
            Environment.getExternalStorageDirectory() + "/DCIM/Camera");
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    //请求状态码
    private static int REQUEST_PERMISSION_CODE = 1;
    private File mCurrentPhotoFile;// 照相机拍照得到的图片
    TextView photoByCamera;
    TextView photoByAblum;
    ImageView imgShow;
    TextView next;
    //判断是否上传图片呢
    boolean isUpload = false;
    String imgPath;
    //后台传输数据
    private static String articleTitle;
    private static int articleKind;

    public static void openAddArticleImg(Context context, String title, int kind){
        Intent intent = new Intent(context,AddArticleImg.class);
        articleTitle = title;
        articleKind = kind;
        context.startActivity(intent);
        ((Activity)context).finish();
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_article_img);
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
//            }
//        }
        initView();

    }
    private void initPhotoError(){
        // android 7.0系统解决拍照的问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
    }
    private void initView(){
        photoByCamera = findViewById(R.id.add_article_addimg1);
        photoByAblum = findViewById(R.id.add_article_addimg2);
        imgShow = findViewById(R.id.add_article_img_show);
        imgShow.setImageResource(R.drawable.uploadimg);
        next = findViewById(R.id.add_article_next);
        photoByCamera.setOnClickListener(this);
        photoByAblum.setOnClickListener(this);
        next.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        if (view.getId()==photoByCamera.getId()){
            openCamera();
        }
        else if (view.getId() == photoByAblum.getId()){
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");// 相片类型
            startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
//            Toast.makeText(AddArticleImg.this,"点击了相册选择",Toast.LENGTH_SHORT).show();
        }
        else if (view.getId()==next.getId()){
//            Toast.makeText(AddArticleImg.this,"点击了下一步",Toast.LENGTH_SHORT).show();
            nextPage();
        }
    }
    protected void openCamera() {
//        try {
//            // Launch camera to take photo for selected contact
//            PHOTO_DIR.mkdirs();// 创建照片的存储目录
//            mCurrentPhotoFile = new File(PHOTO_DIR, getPhotoFileName());// 给新照的照片文件命名
//            final Intent intent = getTakePickIntent(mCurrentPhotoFile);
//            startActivityForResult(intent, REQUEST_CODE_CAPTURE_CAMEIA);
//        } catch (ActivityNotFoundException e) {
//        }
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            Intent getImageByCamera = new Intent("android.media.action.IMAGE_CAPTURE");
            startActivityForResult(getImageByCamera, REQUEST_CODE_CAPTURE_CAMEIA);
        }
        else {
            Toast.makeText(getApplicationContext(), "请确认已经插入SD卡", Toast.LENGTH_LONG).show();
        }
    }
    /**
     * 用当前时间给取得的图片命名
     */
    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "'IMG'_yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date) + ".jpg";
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CODE_PICK_IMAGE) {
            Uri uri = data.getData();
            insertBitmap(getRealFilePath(uri));
        } else if (requestCode == REQUEST_CODE_CAPTURE_CAMEIA) {
            Uri uri = data.getData();
            if(uri == null){
                //use bundle to get data
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    Bitmap photo = (Bitmap) bundle.get("data"); //get bitmap
                    //spath :生成图片取个名字和路径包含类型
                    PHOTO_DIR.mkdirs();
                    String imgPath= PHOTO_DIR.getPath()+getPhotoFileName();
                    saveImage(photo, imgPath);
                    insertBitmap(imgPath);
                } else {
                    Toast.makeText(getApplicationContext(), "系统异常", Toast.LENGTH_LONG).show();
                    return;
                }
            }else{
                insertBitmap(mCurrentPhotoFile.getAbsolutePath());
                //to do find the path of pic by uri
            }

        }
    }
    public static boolean saveImage(Bitmap photo, String spath) {
        try {
            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(spath, false));
            photo.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
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

    /**
     * 添加图片到富文本剪辑器
     *
     * @param imagePath
     */
    private void insertBitmap(String imagePath) {
        File file = new File(imagePath);
        Uri uri = Uri.fromFile(file);
        imgShow.setImageURI(uri);
        imgPath = imagePath;
        isUpload = true;
    }
    private void nextPage(){
        if (isUpload){
            AddArticleModel addArticleModel = new AddArticleModel();
            String imgUrl = "";
            try {
                imgUrl = addArticleModel.uploadImg(imgPath);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            AddArticle.openAddArticle(this,articleTitle,articleKind,imgUrl);
        }
        else Toast.makeText(this,"请上传照片",Toast.LENGTH_SHORT).show();

    }

}
