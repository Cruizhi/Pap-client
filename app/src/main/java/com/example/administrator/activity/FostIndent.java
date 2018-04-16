package com.example.administrator.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;

import com.example.administrator.adapter.IndentAdapter;
import com.example.administrator.bean.Fosterage;
import com.example.administrator.http.ConnectWeb;
import com.example.administrator.http.UploadByServlet;
import com.example.administrator.pap.R;
import com.example.administrator.util.Constant;
import com.example.administrator.util.L;

import org.apache.http.message.BasicNameValuePair;

import java.util.List;

/**
 * Created by Administrator on 2017/11/12.
 */

public class FostIndent extends Activity {

    private ListView listview;
    private List<Fosterage> flist;
    private IndentAdapter myAdapter;

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.viewfostindent);

        initView();
    }

    private void initView(){
        listview = (ListView)findViewById(R.id.lv_fostindent_listview);
        new Thread(new Runnable() {
            @Override
            public void run() {
                BasicNameValuePair bnvp = new BasicNameValuePair("bid", Constant.user.getUserid());
                UploadByServlet.post("UserIndent",bnvp);
                flist = new ConnectWeb().getFostList("UserIndent");
                Message msg = new Message();
                msg.obj = flist;
                handler.sendMessage(msg);
            }
        }).start();
    }

    Handler handler = new Handler(){
        public void handleMessage(Message msg){
            if(flist == null){
                L.i_crz("FostIndent -- 数据集为空");
                return;
            }
            myAdapter = new IndentAdapter(getApplication(),flist,R.layout.item_indent);
            listview.setAdapter(myAdapter);
        }
    };
}
