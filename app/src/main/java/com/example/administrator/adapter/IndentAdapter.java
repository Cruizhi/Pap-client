package com.example.administrator.adapter;

import android.content.Context;

import com.example.administrator.bean.Fosterage;
import com.example.administrator.pap.R;
import com.example.administrator.util.CommonAdapter;
import com.example.administrator.util.ViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/11/21.
 */

public class IndentAdapter extends CommonAdapter<Fosterage> {

    private String url = "http://192.168.101.1:8080/Pap/";

    public IndentAdapter(Context context, List<Fosterage> datas,int itemId){
        super(context,datas,itemId);
    }

    @Override
    public void convert(ViewHolder viewHolder, Fosterage fosterage) {
        viewHolder.setImageURL(context, R.id.im_fostindent_picture,url+fosterage.getDic()+fosterage.getPic())
                .setText(R.id.tv_fostindent_name,fosterage.getTitle())
                .setText(R.id.tv_fostindent_price,String.valueOf(fosterage.getPrice()))
                .setText(R.id.tv_fostindent_day,fosterage.getDay());
    }
}
