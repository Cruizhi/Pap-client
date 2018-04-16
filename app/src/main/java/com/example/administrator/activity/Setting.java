package com.example.administrator.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.administrator.pap.R;
import com.example.administrator.util.Constant;

/**
 * Created by Administrator on 2017/10/20.
 */

public class Setting extends Activity implements View.OnClickListener{

    private LinearLayout Exit;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewsetting);
        initView();
    }

    private void initView(){
        Exit = (LinearLayout)findViewById(R.id.ll_setting_back);
        Exit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_setting_back:
                new AlertDialog.Builder(this).setTitle("提示框").setMessage("是否确定退出")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Constant.user = null;
                            }
                        }).setNegativeButton("取消",null).show();
                break;
            default:
                break;
        }
    }
}
