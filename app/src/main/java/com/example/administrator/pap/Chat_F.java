package com.example.administrator.pap;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.administrator.adapter.ChatAdapter;
import com.example.administrator.bean.Chat;
import com.example.administrator.util.Constant;
import com.example.administrator.util.L;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/10/7.
 */

public class Chat_F extends Fragment implements View.OnClickListener,Runnable{

    private Button Send;  //发送按钮
    private EditText textContent;  //文本输入框
    private ListView listview;
    private ImageButton ImgButton;  //进入聊天室按钮
    private ChatAdapter myAdapter;
    private List<Chat> dataList = new ArrayList<Chat>();  //消息、姓名、时间

    private String contString;  //本机发送的内容
    private String contString1;  //别人发送的内容
    private String IP = "192.168.1.106";
    private int PORT = 8584;
    Socket socket;
    DataInputStream in;
    DataOutputStream out;
    boolean flag = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        //引入布局
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.viewchat,null);
        L.i_crz("Chat_F -- onCreateView");
        //启动activity时不自动弹出软键盘
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        initView(view);
        initData();
        return view;
    }

    //初始化控件
    private void initView(View view){
        listview = (ListView)view.findViewById(R.id.lv_chat_listview);
        Send = (Button)view.findViewById(R.id.bt_chat_send);
        textContent = (EditText)view.findViewById(R.id.et_chat_sendmessage);
        ImgButton = (ImageButton)view.findViewById(R.id.right_btn);
        Send.setOnClickListener(this);
        ImgButton.setOnClickListener(this);
    }

    private void initData(){
        myAdapter = new ChatAdapter(getActivity(),dataList);
        listview.setAdapter(myAdapter);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.bt_chat_send:
                send();
                break;
            case R.id.right_btn:
                //点击进入时，创建子线程连接socket
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            socket = new Socket(IP,PORT);
                            in = new DataInputStream(socket.getInputStream());
                            out = new DataOutputStream(socket.getOutputStream());
                        }catch(IOException e){
                            e.printStackTrace();
                        }
                    }
                }).start();
                flag = true;
                break;
            default :
                break;
        }

    }

    @Override
    public void run() {
        //为true时接受对方发送的信息
        while(true){
            try{
                contString1 = in.readUTF();
                handler.sendMessage(handler.obtainMessage());
                L.i_crz("Chat run---"+contString1);
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    Handler handler = new Handler(){
        public void handleMessage(Message msg){
            String str = contString1;
            L.i_crz("Chat  Handle---"+contString1);
            String name = str.substring(0,str.indexOf("#"));  //获取#号前面的字符串（姓名）
            String text = str.substring(str.indexOf("#")+1);  //获取#号后面的字符串（信息内容）

            //设置对方发送的内容、信息、名称
            Chat entity = new Chat();
            entity.setDate(getDate());
            entity.setName(name);
            entity.setMsgType(true);
            entity.setText(text);

            dataList.add(entity);
            myAdapter.notifyDataSetChanged();
            listview.setSelection(listview.getCount()-1);  //在list view列表的最后一行显示
            super.handleMessage(msg);
        }
    };

    //发送消息
    private void send(){
        contString = textContent.getText().toString();
        //当信息不为空且已经登录时，可发送消息
        if(contString.length() > 0 && flag){
            try{
                //发送信息到socket
                out.writeUTF(Constant.user.getUsername()+"#"+contString);
            }catch(IOException e){
                e.printStackTrace();
            }
            Chat entity = new Chat();
            entity.setDate(getDate());
            entity.setName(Constant.user.getUsername());
            entity.setMsgType(false);
            entity.setText(contString);

            dataList.add(entity);
            myAdapter.notifyDataSetChanged();
            textContent.setText("");
            listview.setSelection(listview.getCount()-1);
        }
    }

    //设置时间显示格式
    private String getDate(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return format.format(new Date(0));
    }
}
