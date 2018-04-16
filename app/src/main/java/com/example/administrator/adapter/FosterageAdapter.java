package com.example.administrator.adapter;

import android.content.Context;

import com.example.administrator.bean.Fosterage;
import com.example.administrator.pap.R;
import com.example.administrator.util.CommonAdapter;
import com.example.administrator.util.ViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/9/22.
 */

public class FosterageAdapter extends CommonAdapter<Fosterage> {

    private String url = "http://192.168.101.1:8080/Pap/";

    public FosterageAdapter(Context context, List<Fosterage> datas, int itemLayoutResId){
        super(context,datas,itemLayoutResId);
    }

    @Override
    public void convert(ViewHolder viewHolder, Fosterage item) {
            viewHolder.setImageURL(context,R.id.im_fosterage_picture,url+item.getDic()+item.getPic())
                    .setText(R.id.tv_fosterage_title, item.getTitle())
                    .setText(R.id.tv_fosterage_address, item.getDes())
                    .setText(R.id.tv_fosterage_date,item.getDate());
    }
}
