package com.example.administrator.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.administrator.http.ConnectWeb;
import com.example.administrator.http.UploadByServlet;
import com.example.administrator.pap.MainActivity;
import com.example.administrator.pap.R;
import com.example.administrator.util.Constant;

import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2017/11/12.
 */

public class FostInfo extends Activity implements View.OnClickListener{

    private final static int TAKEPHOTO = 1;
    private final static int CHOOSE_PHOTO = 2;
    private final static int CROP_PHOTO = 3;
    private String imageName = "";
    private String imagepath = "";
    private EditText EtTitle;
    private EditText EtDay;
    private EditText EtRequire;
    private EditText EtPrice;
    private ImageView picture;
    private Button Sure;
    private Button BtTakephoto;  //拍照
    private Button BtChooseFromAlbum;  //从相册中选取照片
    private Uri imageUri;
    private String url = "FostInfo";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewfostinfo);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads().detectDiskWrites().detectNetwork()
                .penaltyLog().build());


        initView();
        initEvent();
    }

    private void initView(){
        EtTitle = (EditText)findViewById(R.id.et_fostinfo_name);
        EtPrice = (EditText)findViewById(R.id.et_fostinfo_price);
        EtDay = (EditText)findViewById(R.id.et_fostinfo_day);
        picture = (ImageView)findViewById(R.id.im_fostinfo_picture);
        EtRequire = (EditText)findViewById(R.id.et_fostinfo_require);
        Sure = (Button)findViewById(R.id.bt_fostinfo_sure);
        BtTakephoto = (Button)findViewById(R.id.bt_fostinfo_takephoto);
        BtChooseFromAlbum = (Button)findViewById(R.id.bt_fostinfo_chooseFromAlbum);
    }

    private void initEvent(){
        BtChooseFromAlbum.setOnClickListener(this);
        BtTakephoto.setOnClickListener(this);
        Sure.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.bt_fostinfo_takephoto:
                Date date = new Date();
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
                String str = format.format(date);
                String ImageName = str+".jpg";
                File outputImage = new File(Environment.getExternalStorageDirectory(),ImageName);
                try{
                    if(outputImage.exists()){
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                }catch(IOException e){
                    e.printStackTrace();
                }
                imageUri = Uri.fromFile(outputImage);
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                startActivityForResult(intent,TAKEPHOTO);  //启动相机
                break;
            case R.id.bt_fostinfo_chooseFromAlbum:
                Intent intent1 = new Intent("android.intent.action.GET_CONTENT");
                intent1.setType("image/*");  //打开相册
                startActivityForResult(intent1,CHOOSE_PHOTO);
                break;
            case R.id.bt_fostinfo_sure:
                ConnectWeb.uploadFile(imagepath,imageName);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        BasicNameValuePair bnvp0 = new BasicNameValuePair("uid", Constant.user.getUserid());
                        BasicNameValuePair bnvp1 = new BasicNameValuePair("price",EtPrice.getText().toString());
                        BasicNameValuePair bnvp2 = new BasicNameValuePair("name",EtTitle.getText().toString());
                        BasicNameValuePair bnvp3 = new BasicNameValuePair("require",EtRequire.getText().toString());
                        BasicNameValuePair bnvp4 = new BasicNameValuePair("day",EtDay.getText().toString());
                        BasicNameValuePair bnvp5 = new BasicNameValuePair("dic",imagepath);
                        BasicNameValuePair bnvp6 = new BasicNameValuePair("pic",imageName);
                        BasicNameValuePair bnvp7 = new BasicNameValuePair("date",getTime());
                        BasicNameValuePair bnvp8 = new BasicNameValuePair("address",Constant.user.getAddress());
                        String resposne = UploadByServlet.post(url,bnvp0,bnvp1,bnvp2,bnvp3,bnvp4,bnvp5,bnvp6,bnvp7,bnvp8);
                        Message msg = new Message();
                        msg.obj = resposne;
                        handle.sendMessage(msg);
                    }
                }).start();
                Intent intent2 = new Intent(FostInfo.this, MainActivity.class);
                startActivity(intent2);
                this.finish();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        switch (requestCode){
            case TAKEPHOTO:
                if(resultCode == RESULT_OK){
                    Intent intent = new Intent("com.android.camera.action.CROP");
                    intent.setDataAndType(imageUri,"image/*");
                    intent.putExtra("scale",true);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                    startActivityForResult(intent,CROP_PHOTO);  //启动裁剪程序
                }
                break;
            case CROP_PHOTO:
                if(resultCode == RESULT_OK){
                    try{
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        picture.setImageBitmap(bitmap);
                    }catch(FileNotFoundException e){
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO:
                if(resultCode == RESULT_OK){
                    //判断打手机型号
                    if(Build.VERSION.SDK_INT >= 19){
                        //4.4以上的系统
                        handleImageOnKitKat(data);
                    }else{
                        //4.4以下的系统
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            default :
                break;

        }
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data){
        String imagePath = null;
        Uri uri = data.getData();
        if(DocumentsContract.isDocumentUri(this,uri)){
            String docId = DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id = docId.split(":")[1];  //解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "="+id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(docId));
                imagePath = getImagePath(contentUri,null);
            }
        }else if("content".equalsIgnoreCase(uri.getScheme())){
            //如果不是document类型的uri，就使用普通方式处理
            imagePath = getImagePath(uri,null);
        }
        imagepath = imagePath;
        displayImage(imagePath);  //根据图片路径显示图片
    }

    private void handleImageBeforeKitKat(Intent data){
        Uri uri = data.getData();
        String imagePath = getImagePath(uri,null);
        imagepath = imagePath;
        displayImage(imagePath);
    }

    //获取图片路径
    private String getImagePath(Uri uri, String selection){
        String path =null;
        Cursor cursor = getContentResolver().query(uri,null,selection,null,null);
        if(cursor != null ){
            if(cursor.moveToFirst()){
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    //根据图片路径显示图片
    private void displayImage(String imagePath){
        String temp[] = imagePath.replaceAll("\\\\","/").split("/");  //保留路径中最后一个/后面的内容
        if(imagePath != null){
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            picture.setImageBitmap(bitmap);
            if(temp.length > 1){
                imageName = temp[temp.length - 1];
            }
        }else{
            Toast.makeText(this,"failed tp get image",Toast.LENGTH_SHORT);
        }
    }

    private Handler handle = new Handler(){
        public void handleMessage(Message msg){
            String response =(String) msg.obj;
            if(response.equals("true")){
                Toast.makeText(FostInfo.this,"上架成功",Toast.LENGTH_SHORT);
                Intent intent = new Intent(FostInfo.this,MainActivity.class);
                startActivity(intent);
            }
        }
    };

    private String getTime(){
        return new SimpleDateFormat("MM-dd HH.mm", Locale.CANADA).format(new Date());
    }
}
