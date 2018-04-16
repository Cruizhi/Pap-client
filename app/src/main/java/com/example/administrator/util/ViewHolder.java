package com.example.administrator.util;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Administrator on 2017/9/20.
 */

public class ViewHolder {
    private SparseArray<View> mViews;
    private int mPosition = 0;
    private View mConvertView;


    /*
       存储item中所用控件引用容器

       Key - 资源ID
       Value - 控件的引用
     */

    private ViewHolder(Context context, ViewGroup parent,int layoutId,int position){
        this.mPosition = position;
        this.mViews = new SparseArray<View>();
        mConvertView = LayoutInflater.from(context).inflate(layoutId,parent,false);
        mConvertView.setTag(this);
    }

    //判断原来是否有convertView
    public static ViewHolder get(Context context,View convertView,
                                 ViewGroup parent, int layoutId,int position){
        if(convertView == null){
            return new ViewHolder(context,parent,layoutId,position);
        }else{
            ViewHolder holder = (ViewHolder)convertView.getTag();
            holder.mPosition = position;
            return holder;
        }
    }

    //通过viewIdu获取控件
    public <T extends View> T getView(int viewId){
        View view = mViews.get(viewId);
        if(view == null){
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId,view);
        }
        return (T) view;
    }

    public View getConvertView(){
        return mConvertView;
    }

    public ViewHolder setText(int viewId,String text){
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }

    public ViewHolder setImageURL(Context context,int viewId,String url){
        ImageView view = getView(viewId);
        Glide.with(context).load(url).into(view);
        return this;
    }

    public ViewHolder setCheckBox(int viewId, List<Boolean> mChecked){
        CheckBox cb = getView(viewId);
        final List<Boolean> Checked = mChecked;
//        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                Checked.set(mPosition,isChecked);
//            }
//        });
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb1 = (CheckBox)v;
                Checked.set(mPosition,cb1.isChecked());
            }
        });
        return this;

    }

    /////////////////////////////////////////////////////////////////////
//    public ViewHolder setCheckBox(int viewId,final onCheckedChanged listener){
//        CheckBox cb = getView(viewId);
//        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton arg0, boolean choice) {
//                listener.getChoiceData(mPosition, choice);
//            }
//        });
//        return this;
//    }
//
//    public interface onCheckedChanged{
//
//        public void getChoiceData(int position,boolean isChoice);
//
//    }
//    public void setOnCheckedChanged(onCheckedChanged listener){
//        this.listener=listener;
//    }
}
