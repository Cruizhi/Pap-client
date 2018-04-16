package com.example.administrator.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.bean.Goods;
import com.example.administrator.pap.R;
import com.example.administrator.util.Constant;
import com.example.administrator.util.L;

/**
 * Created by Administrator on 2017/10/1.
 */

public class Indent extends Activity {

    private TextView address;
    private ImageView picture;
    private TextView name;
    private TextView price;
    private TextView ok;

    private String url = "http://192.168.101.1:8080/Pap/";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewindent);
        initView();
        initData();
    }

    private void initView(){
        address = (TextView)findViewById(R.id.tv_indent_address);
        picture = (ImageView)findViewById(R.id.im_indent_picture);
        name = (TextView)findViewById(R.id.tv_indent_name);
        price = (TextView)findViewById(R.id.tv_indent_price);
        ok = (TextView)findViewById(R.id.tv_indent_ok);

    }

    private void initData(){
        Bundle bundle = this.getIntent().getExtras();  //获取Bundle
        final Goods theGoods = (Goods) bundle.getSerializable("GoodObj");
        address.setText(Constant.user.getAddress());
        name.setText(theGoods.getBrand());
        price.setText(String.valueOf(theGoods.getPrice()));
        Glide.with(this).load(url+theGoods.getDir()+theGoods.getPic()).into(picture);
        L.i_crz("Indent-initDate--"+Constant.user.getAddress());
    }
}
