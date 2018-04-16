package com.example.administrator.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.administrator.bean.Fosterage;
import com.example.administrator.http.UploadByServlet;
import com.example.administrator.pap.MainActivity;
import com.example.administrator.pap.R;
import com.example.administrator.util.Constant;

import org.apache.http.message.BasicNameValuePair;

/**
 * Created by Administrator on 2017/11/19.
 */

public class FosterageDetail extends Activity {

    private TextView Title;
    private TextView Price;
    private TextView Day;
    private TextView Require;
    private Button Accept;
    private ImageView Picture;

    public int id;

    private String url = "FostIndent";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewfostdetail);

        initView();
        initEvent();
        getFosterageDetail();
    }

    private void initView(){
        Title = (TextView)findViewById(R.id.tv_fost_title);
        Price = (TextView)findViewById(R.id.tv_fost_price);
        Day = (TextView)findViewById(R.id.tv_fost_day);
        Require = (TextView)findViewById(R.id.tv_fost_require);
        Accept = (Button)findViewById(R.id.bt_fost_accept);
        Picture = (ImageView)findViewById(R.id.im_fost_picture);
    }

    private void getFosterageDetail(){
        String url = "http://192.168.101.1:8080/Pap/";
        Bundle bundle = this.getIntent().getExtras();  //获取Bundle
        final Fosterage fosterage = (Fosterage) bundle.getSerializable("FostObj");  //获取Bundle中商品对象
        //获取资源文件种中的Text View
        Title.setText(fosterage.getTitle());  //商品名字
        Price.setText("价格￥:"+fosterage.getPrice());  //商品价格
        Day.setText("寄养天数:"+fosterage.getDay());
        Require.setText(fosterage.getDes());
        id = fosterage.getId();
        Glide.with(this).load(url+fosterage.getDic()+fosterage.getPic()).into(Picture);
    }

    private void initEvent(){
        Accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        BasicNameValuePair bnvp0 = new BasicNameValuePair("id",String.valueOf(id));
                        BasicNameValuePair bnvp1 = new BasicNameValuePair("bid", Constant.user.getUserid());
                        String response = UploadByServlet.post(url,bnvp0,bnvp1);
                        Message msg = new Message();
                        msg.obj = response;
                        handler.sendMessage(msg);
                    }
                }).start();
            }
        });
    }

    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            String response =(String) msg.obj;
            if(response.equals("true")){
                Toast.makeText(getApplication(),"已接受订单",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(FosterageDetail.this, MainActivity.class);
                startActivity(intent);
                finish();
            }else{
                Toast.makeText(getApplication(),"接受失败",Toast.LENGTH_SHORT).show();
            }
        }
    };

}
