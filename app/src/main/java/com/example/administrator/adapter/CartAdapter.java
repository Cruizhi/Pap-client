package com.example.administrator.adapter;

import android.content.Context;

import com.example.administrator.bean.Cart;
import com.example.administrator.pap.R;
import com.example.administrator.util.CommonAdapter;
import com.example.administrator.util.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/2.
 */

public class CartAdapter extends CommonAdapter<Cart>{

    public List<Boolean> mChecked = new ArrayList<Boolean>();
//    private ViewHolder.onCheckedChanged listener;

    private String url = "http://192.168.101.1:8080/Pap/";

    public CartAdapter(Context context, List<Cart> datas, int itemLayoutResId){
        super(context,datas,itemLayoutResId);

        for(int i = 0;i<datas.size();i++){
            mChecked.add(false);
        }
    }

//        public void setOnCheckedChanged(ViewHolder.onCheckedChanged listener){
//        this.listener=listener;
//    }

    @Override
    public void convert(ViewHolder viewHolder, Cart cart) {
        viewHolder.setImageURL(context, R.id.im_cart_picture,url+cart.getDir()+cart.getPic())
                .setText(R.id.tv_cart_name,cart.getBrand())
                .setText(R.id.tv_cart_price,"￥:"+String.valueOf(cart.getPrice()))
                .setCheckBox(R.id.cb_cart_choose,mChecked);
                //.setCheckBox(R.id.cb_cart_choose,listener);


    }

}
