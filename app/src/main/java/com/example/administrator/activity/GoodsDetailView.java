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
import com.example.administrator.bean.Goods;
import com.example.administrator.http.UploadByServlet;
import com.example.administrator.pap.R;
import com.example.administrator.util.Constant;

import org.apache.http.message.BasicNameValuePair;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/9/4.
 */

public class GoodsDetailView extends Activity implements View.OnClickListener{

    private TextView TvBrand;
    private TextView TvPrice;
    private TextView TvBcount;
    private TextView TvDes;
    private ImageView ImPicture;
    private Button AddtoCart;
    private Button Buy;

    private String Url = "AddCart";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewgoodsdetail);
        initView();
        getGoodsDetail();  //获取商品详情
    }

    private void initView(){
        TvBrand = (TextView)findViewById(R.id.tv_goods_brand);
        TvPrice = (TextView)findViewById(R.id.tv_goods_price);
        TvBcount = (TextView)findViewById(R.id.tv_goods_bcount);
        TvDes = (TextView)findViewById(R.id.tv_goods_des);
        ImPicture = (ImageView)findViewById(R.id.im_goods_picture);
        AddtoCart = (Button)findViewById(R.id.bt_goods_addtocart);
        Buy = (Button)findViewById(R.id.bt_goods_buy);
        //加入购物车
        AddtoCart.setOnClickListener(this);
        Buy.setOnClickListener(this);
    }

    public void getGoodsDetail(){

        String url = "http://192.168.101.1:8080/Pap/";
        Bundle bundle = this.getIntent().getExtras();  //获取Bundle
        final Goods theGoods = (Goods) bundle.getSerializable("GoodObj");  //获取Bundle中商品对象
        //获取资源文件种中的Text View
        String brand = String.valueOf(theGoods.getBcount());
        TvBrand.setText(theGoods.getBrand());  //商品名字
        TvPrice.setText("价格￥:"+theGoods.getPrice());  //商品价格
        TvBcount.setText("月销"+brand);
        TvDes.setText(theGoods.getDes());
        Glide.with(this).load(url+theGoods.getDir()+theGoods.getPic()).into(ImPicture);
//        try{
//            URL picUrl = new URL(url+theGoods.getDir()+theGoods.getPic());
//            Bitmap pngbm = BitmapFactory.decodeStream(picUrl.openStream());
//            ImPicture.setImageBitmap(pngbm);
//        }catch(Exception e){
//            e.printStackTrace();
//        }

    }

    @Override
    public void onClick(View v) {
        Bundle bundle = this.getIntent().getExtras();  //获取Bundle
        final Goods theGoods = (Goods) bundle.getSerializable("GoodObj");
        switch(v.getId()){
            case R.id.bt_goods_addtocart:
                //把商品信息传递到服务器的购物车数据库中
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        BasicNameValuePair bnvp0 = new BasicNameValuePair("userid", Constant.user.getUserid());
                        BasicNameValuePair bnvp1 = new BasicNameValuePair("buyid",String.valueOf(theGoods.getUid()));
                        BasicNameValuePair bnvp2 = new BasicNameValuePair("brand",theGoods.getBrand());
                        BasicNameValuePair bnvp3 = new BasicNameValuePair("des",theGoods.getDes());
                        BasicNameValuePair bnvp4 = new BasicNameValuePair("price",String.valueOf(theGoods.getPrice()));
                        BasicNameValuePair bnvp5 = new BasicNameValuePair("dir",theGoods.getDir());
                        BasicNameValuePair bnvp6 = new BasicNameValuePair("pic",theGoods.getPic());
                        BasicNameValuePair bnvp7 = new BasicNameValuePair("bcount",String.valueOf(theGoods.getBcount()));
                        String response = UploadByServlet.post(Url,bnvp0,bnvp1,bnvp2,bnvp3,bnvp4,bnvp5,bnvp6,bnvp7);
                        Message msg = new Message();
                        msg.obj = response;
                        handler.sendMessage(msg);
                    }
                }).start();
                break;
            case R.id.bt_goods_buy:
                Intent intent = new Intent();
                Bundle bundle1 = new Bundle();  //创建Bundle对象
                intent.setClass(GoodsDetailView.this,Indent.class);
                bundle1.putSerializable("GoodObj", (Serializable) theGoods);
                intent.putExtras(bundle1);
                startActivity(intent);
                break;
            default:
                break;
        }

    }

    Handler handler = new Handler(){
        public void handleMessage(Message msg){
            String response = (String)msg.obj;
            if(response.equals("true")){
                Toast.makeText(getApplication(),"成功添加到购物车",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplication(),"添加失败",Toast.LENGTH_SHORT).show();
            }
        }
    };
}
