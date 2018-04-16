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
import android.view.View.OnClickListener;
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

/**
 * Created by Administrator on 2017/9/11.
 */

public class SalesGoods extends Activity implements OnClickListener {

    private final static int TAKEPHOTO = 1;
    private final static int CHOOSE_PHOTO = 2;
    private final static int CROP_PHOTO = 3;
    private String imageName = "";
    private String imagepath = "";
    private Uri imageUri;  //拍照图片位置
    private EditText EtBrand;  //商品名称
    private EditText EtPrice;  //商品价格
    private EditText EtDes;  //商品详情
    private ImageView picture;  //商品图片
    private Button BtTakephoto;  //拍照
    private Button BtChooseFromAlbum;  //从相册中选取照片
    private Button BtSure;  //确定
    //private List<Constant> userlist = new Login().Reuser();  //获取用户信息
    private String url = "SalesGoods";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewsales);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads().detectDiskWrites().detectNetwork()
                .penaltyLog().build());

        initView();
        initEvents();
    }

    //初始化控件
    private void initView(){
        picture = (ImageView)findViewById(R.id.im_sales_picture);
        EtBrand = (EditText)findViewById(R.id.et_sales_name);
        EtPrice = (EditText)findViewById(R.id.et_sales_price);
        EtDes = (EditText)findViewById(R.id.et_sales_des);
        BtTakephoto = (Button)findViewById(R.id.bt_sales_takephoto);
        BtChooseFromAlbum = (Button)findViewById(R.id.bt_sales_chooseFromAlbum);
        BtSure = (Button) findViewById(R.id.bt_sales_sure);
    }

    //绑定监听事件
    private void initEvents(){
        BtTakephoto.setOnClickListener(this);
        BtChooseFromAlbum.setOnClickListener(this);
        BtSure.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.bt_sales_takephoto:
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
            case R.id.bt_sales_chooseFromAlbum:
                Intent intent1 = new Intent("android.intent.action.GET_CONTENT");
                intent1.setType("image/*");  //打开相册
                startActivityForResult(intent1,CHOOSE_PHOTO);
                break;
            case R.id.bt_sales_sure:
                ConnectWeb.uploadFile(imagepath,imageName);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        BasicNameValuePair bnvp0 = new BasicNameValuePair("uid", Constant.user.getUserid());  //userlist.get(0).getUerid().toString()
                        BasicNameValuePair bnvp1 = new BasicNameValuePair("brand",EtBrand.getText().toString());
                        BasicNameValuePair bnvp2 = new BasicNameValuePair("price",EtPrice.getText().toString());
                        BasicNameValuePair bnvp3 = new BasicNameValuePair("des",EtDes.getText().toString());
                        BasicNameValuePair bnvp4 = new BasicNameValuePair("dic",imagepath);
                        BasicNameValuePair bnvp5 = new BasicNameValuePair("pic",imageName);
                        BasicNameValuePair bnvp6 = new BasicNameValuePair("bcount","0");
                        String response = UploadByServlet.post(url,bnvp0,bnvp1,bnvp2,bnvp3,bnvp4,bnvp5,bnvp6);
                        Message msg = new Message();
                        msg.obj = response;
                        msg.what = 1;
                        handler.sendMessage(msg);
                    }
                }).start();
                Intent intent2 = new Intent(SalesGoods.this,MainActivity.class);
                startActivity(intent2);
                finish();
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

    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch(msg.what){
                case 1:
                    String response =(String) msg.obj;
                    if(response.equals("true")){
                        Toast.makeText(SalesGoods.this,"上架成功",Toast.LENGTH_SHORT);
                        Intent intent = new Intent(SalesGoods.this,MainActivity.class);
                        startActivity(intent);
                    }
                    break;
                default:
                    break;
            }
        }
    };

}
