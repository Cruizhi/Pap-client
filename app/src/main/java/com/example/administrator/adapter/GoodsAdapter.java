package com.example.administrator.adapter;

import android.content.Context;

import com.example.administrator.bean.Goods;
import com.example.administrator.pap.R;
import com.example.administrator.util.CommonAdapter;
import com.example.administrator.util.ViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2017/9/29.
 */

public class GoodsAdapter extends CommonAdapter<Goods> {

    private String url = "http://192.168.101.1:8080/Pap/";

    public GoodsAdapter(Context context, List<Goods> datas,int itemLayoutResId){
        super(context,datas,itemLayoutResId);
    }

    @Override
    public void convert(ViewHolder viewHolder, Goods goods) {
        viewHolder.setImageURL(context, R.id.im_shouye_img,url+ goods.getDir()+goods.getPic())
                .setText(R.id.tv_shouye_title,goods.getBrand())
                .setText(R.id.tv_shouye_price,String.valueOf(goods.getPrice()));
    }
}
