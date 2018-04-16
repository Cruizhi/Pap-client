package com.example.administrator.pap;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.example.administrator.activity.FosterageDetail;
import com.example.administrator.adapter.FosterageAdapter;
import com.example.administrator.bean.Fosterage;
import com.example.administrator.http.ConnectWeb;
import com.example.administrator.util.L;
import com.example.administrator.widget.XListView;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.example.administrator.pap.R.layout.item_fosterage;

/**
 * Created by Administrator on 2017/9/19.
 */

public class Fosterage_F extends Fragment implements XListView.IXListViewListener{

    private XListView listview;
    private Handler mHandler;
    private int mIndex = 0;
    private int mRefreshIndex = 0;

    private FosterageAdapter myAdapter;
    private List<Fosterage> fostList = new ArrayList<Fosterage>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        //引入布局
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.viewfosterage,null);
        L.i_crz("Fosterage_F -- onCreateView");
        initView(view);
        return view;
    }

    private void initView(View view){
        listview = (XListView)view.findViewById(R.id.lv_fosterage_listview);
        listview.setPullRefreshEnable(true);
        listview.setPullLoadEnable(true);
        listview.setAutoLoadEnable(true);
        listview.setXListViewListener(this);
        listview.setRefreshTime(getTime());

        mHandler = new Handler();
        //myAdapter = new FosterageAdapter(getActivity(),goodsList,R.layout.item_fosterage);
        //adapter = new MyAdapter(getActivity(),goodsList,R.layout.text);
        //listview.setAdapter(adapter);
        //子线程获取列表
        new Thread(new Runnable() {
            @Override
            public void run() {
                L.i_crz("Fosterage_F -- getFosterageList");
                getFosterageList();   //获取寄养列表
            }
        }).start();
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                int position, long id) {
                    L.i_crz("Fosterage_F position:"+position);
                    // TODO Auto-generated method stub
                    Fosterage foster = fostList.get(position-1);  //获取列表当前点中的商品
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();  //创建Bundle对象
                    intent.setClass(getActivity(),FosterageDetail.class);
                    bundle.putSerializable("FostObj", (Serializable) foster);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
        });
    }

    private void getFosterageList(){
        new Thread(){
            public void run(){
                try{
                    L.i_crz("Fosterage_F -- getFosterageList2");
                    fostList = new ConnectWeb().getFostList("Fosterage");
                    Message m = new Message();
                    m.obj = fostList;
                    handler.sendMessage(m);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }.start();
    }

    Handler handler = new Handler(){
        public void handleMessage(Message msg){
            if(fostList == null){
                L.i_crz("Fosterage_F -- 数据集为空");
                return;
            }
            L.i_crz("Fosterage_F -- Handle;fostlist.size:"+fostList.size());
            //myAdapter.notifyDataSetChanged();
            //adapter.notifyDataSetChanged();
            myAdapter = new FosterageAdapter(getActivity(),fostList, item_fosterage);
            //adapter = new MyAdapter(getActivity(),goodsList,R.layout.text);
            listview.setAdapter(myAdapter);
        }
    };

    //@Override切换窗口时实现自动刷新
    public void onWindowFocusChanged(boolean hasFocus){
        super.getActivity().onWindowFocusChanged(hasFocus);
        if(hasFocus){
            listview.autoRefresh();
        }
    }


    @Override
    public void onRefresh() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mIndex = ++mRefreshIndex;
                fostList.clear();
                getFosterageList();
                myAdapter = new FosterageAdapter(getActivity(), fostList, item_fosterage);
                listview.setAdapter(myAdapter);
                onLoad();
            }
        },2500);

    }

    @Override
    public void onLoadMore() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getFosterageList();
                myAdapter.notifyDataSetChanged();
                onLoad();
            }
        },2500);

    }

    private void onLoad(){
        listview.stopRefresh();
        listview.stopLoadMore();
        listview.setRefreshTime(getTime());
    }

    private String getTime(){
        return new SimpleDateFormat("MM-dd HH.mm", Locale.CANADA).format(new Date());
    }


//    public void showFosterageList( ){
//        L.i_crz("Fosterage_F -- showFosterageList");
//        new Thread(){
//            @Override
//            public void run(){
//                myAdapter = new FosterageAdapter(getActivity(),goodsList,R.layout.item_fosterage);
//                L.i_crz("F -- "+goodsList.size());
//            }
//        }.start();
//        listview.setAdapter(myAdapter);
//    }
}
