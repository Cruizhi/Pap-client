package com.example.administrator.pap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.activity.ChoiceLogin;
import com.example.administrator.activity.FostIndent;
import com.example.administrator.activity.FostInfo;
import com.example.administrator.activity.SalesGoods;
import com.example.administrator.activity.Setting;
import com.example.administrator.util.Constant;
import com.example.administrator.util.L;

/**
 * Created by Administrator on 2017/9/4.
 */

public class User_F extends Fragment implements View.OnClickListener{

    private ImageView Head;
    private Button BtSell;
    private Button BtSet;
    private Button Fosterage;
    private Button FostIndent;
    private Button FinishIndent;
    private TextView Uname;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //引入布局
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.viewuser, null);

        //设置全屏
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(lp);

        initView(view);
        return view;
    }

    private void initView(View view){
        Head = (ImageView)view.findViewById(R.id.iv_user_head);
        BtSell = (Button)view.findViewById(R.id.bt_user_sell);
        BtSet = (Button)view.findViewById(R.id.bt_user_set);
        Uname = (TextView)view.findViewById(R.id.tv_user_name);
        Fosterage = (Button)view.findViewById(R.id.bt_user_fosterage);
        FostIndent = (Button)view.findViewById(R.id.bt_user_fosterageindent);
        FinishIndent = (Button)view.findViewById(R.id.bt_user_finishindent);
        Head.setOnClickListener(this);
        BtSell.setOnClickListener(this);
        BtSet.setOnClickListener(this);
        Fosterage.setOnClickListener(this);
        FostIndent.setOnClickListener(this);
        FinishIndent.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.bt_user_sell:
                Intent intent = new Intent(getActivity(),SalesGoods.class);
                startActivity(intent);
                break;
            case R.id.bt_user_set:
                Intent intent1 = new Intent(getActivity(), Setting.class);
                startActivity(intent1);
                break;
            case R.id.iv_user_head:
                String data = Constant.user.getUserid();
                if(data == null){
                    L.i_crz("User_F -- 点击" + data);
                    Intent intent2 = new Intent(getActivity(), ChoiceLogin.class);
                    startActivity(intent2);
                }else{
                    Uname.setText(Constant.user.getUsername());
                    //显示头像*********************************
                }
                break;
            case R.id.bt_user_fosterage:
                Intent intent3 = new Intent(getActivity(), FostInfo.class);
                startActivity(intent3);
                break;
            case R.id.bt_user_fosterageindent:
                Intent intent4 = new Intent(getActivity(), FostIndent.class);
                startActivity(intent4);
                break;
            case R.id.bt_user_finishindent:
                Intent intent5 = new Intent(getActivity(), com.example.administrator.activity.FinishIndent.class);
                startActivity(intent5);
                break;
            default :
                break;
        }
    }
}
