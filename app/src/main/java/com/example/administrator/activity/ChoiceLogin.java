package com.example.administrator.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.administrator.pap.R;

/**
 * Created by Administrator on 2017/9/3.
 */

public class ChoiceLogin extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choice);
    }

    // 登录
    public void welcome_login(View v) {
        Intent intent = new Intent();
        intent.setClass(ChoiceLogin.this, Login.class);
        startActivity(intent);
        this.finish();
    }

    //注册
    public void welcome_register(View v){
        Intent intent = new Intent();
        intent.setClass(ChoiceLogin.this,Register.class);
        startActivity(intent);
        this.finish();
    }

}
