package com.example.administrator.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.adapter.GoodsAdapter;
import com.example.administrator.bean.Goods;
import com.example.administrator.http.UploadByServlet;
import com.example.administrator.pap.R;
import com.example.administrator.util.L;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/4.
 */

public class WareActivity extends Activity implements View.OnClickListener{

    private EditText Searchedit;  //输入搜索内容
    private TextView Search;  //搜索按钮
    private ListView listview;
    private ImageView Back;

    private List<Goods> myList = new ArrayList<Goods>();
    private GoodsAdapter myAdapter;

    private String url ="Ware";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewware);
        initView();

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                Goods theGood = myList.get(position);  //获取列表当前点中的商品
                Intent intent = new Intent();
                Bundle bundle = new Bundle();  //创建Bundle对象
                intent.setClass(WareActivity.this,GoodsDetailView.class);
                bundle.putSerializable("GoodObj", (Serializable) theGood);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        myAdapter = new GoodsAdapter(this,myList,R.layout.item_shouye);
        listview.setAdapter(myAdapter);
    }

    private void initView(){
        Searchedit = (EditText)findViewById(R.id.et_ware_searchware);
        listview = (ListView)findViewById(R.id.lv_ware_listview);
        Search = (TextView)findViewById(R.id.tv_ware_search);
        Search.setOnClickListener(this);
        Back = (ImageView)findViewById(R.id.iv_ware_back);
        Back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.tv_ware_search:
                myList.clear();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        BasicNameValuePair bnvp = new BasicNameValuePair("brand",Searchedit.getText().toString());
                        String response = UploadByServlet.post(url,bnvp);
                        Message msg = new Message();
                        msg.obj = response;
                        handler.sendMessage(msg);
                        L.i_crz(Searchedit.getText().toString());
                    }
                }).start();
                break;
            case R.id.iv_ware_back:
                this.finish();
                break;
            default:
                break;
        }
    }

    Handler handler = new Handler(){
        public void handleMessage(Message msg){
            String response =(String)msg.obj;
            JSONArray ja = null;
            try {
                ja = new JSONArray(response);
                for(int i= 0;i <ja.length();i++){
                    JSONObject jo = ja.getJSONObject(i);
                    Goods goods = new Goods();
                    goods.setBrand(jo.getString("brand"));
                    goods.setBcount(jo.getInt("bcount"));
                    goods.setPrice(jo.getDouble("price"));
                    goods.setDes(jo.getString("des"));
                    goods.setDir(jo.getString("dir"));
                    goods.setPic(jo.getString("pic"));
                    myList.add(goods);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            myAdapter.notifyDataSetChanged();
        }
    };
}
