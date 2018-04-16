package com.example.administrator.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import java.io.IOException;
import java.net.URL;

/**
 * Created by Administrator on 2017/9/18.
 */

public class Text extends Activity {

    private ImageView imageView;
    private String url = "http://192.168.101.1:8080/Pap/img/2/4/d4f1120a-da8b-4be9-aefb-565857476a79_jiemian.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.text);
        //imageView = (ImageView)findViewById(R.id.im_text_picture);
        new Thread(networkTask).start();
    }

    Runnable networkTask = new Runnable() {
        @Override
        public void run() {
            Message msg = new Message();
            Bundle data = new Bundle();
            data.putString("value", url);
            msg.setData(data);
            handler.sendMessage(msg);
        }
    };

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("value");
            try {
                URL picUrl = new URL(val);
                Bitmap pngbm = BitmapFactory.decodeStream(picUrl.openStream());
                imageView.setImageBitmap(pngbm);
            }catch(IOException e ){
                e.printStackTrace();
            }
            // TODO
            // UI界面的更新等相关操作
        }
    };

}
