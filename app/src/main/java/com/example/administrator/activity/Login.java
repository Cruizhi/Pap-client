package com.example.administrator.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.http.ConnectWeb;
import com.example.administrator.http.UploadByServlet;
import com.example.administrator.pap.MainActivity;
import com.example.administrator.pap.R;
import com.example.administrator.util.Constant;
import com.example.administrator.util.L;

import org.apache.http.message.BasicNameValuePair;

/**
 * Created by Administrator on 2017/9/3.
 */

public class Login extends Activity implements View.OnClickListener{


    public static Context mContext;

    private EditText userid; //账号编辑框
    private EditText password; //密码编辑框
    private Button dologin;//登录按钮

    private String url = "Login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        initView(); //初始化控件
        dologin.setOnClickListener(this);
    }

    public void initView(){
        userid = (EditText)findViewById(R.id.login_id_edit);
        password = (EditText)findViewById(R.id.login_password_edit);
        dologin = (Button)findViewById(R.id.login_login_btn);
    }

    //忘记密码
    public void login_forget(View view){

    }



    //回退
    public void login_back(View view){
        this.finish();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.login_login_btn:
                String uid = userid.getText().toString();
                String passwd = password.getText().toString();
                if(!"".equals(uid) && !"".equals(passwd)) {
//                    Constant.user.setUserid(uid);   //保存当前用户id，用于判断用户是否有登录
//                    Constant.user.setPassword(passwd);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            BasicNameValuePair bnvp0 = new BasicNameValuePair("userid", userid.getText().toString());
                            BasicNameValuePair bnvp1 = new BasicNameValuePair("password", password.getText().toString());
                            String response = UploadByServlet.post(url, bnvp0, bnvp1);
                            Message msg = new Message();
                            msg.obj = response;
                            msg.what = 1;
                            handler.sendMessage(msg);
                        }
                    }).start();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            BasicNameValuePair bnvp = new BasicNameValuePair("userid", userid.getText().toString());
                            String response1 = UploadByServlet.post("User",bnvp);
                            ConnectWeb conn = new ConnectWeb();
                            Constant.user =conn.getUserinformation(response1);
                            L.i_crz("getUserinformation----"+Constant.user);
                            L.i_crz("Login useraddress---"+Constant.user.getUserid()+Constant.user.getAddress());
                        }
                    }).start();
                    this.finish();
                }else if("".equals(uid)||"".equals(passwd)){
                    new AlertDialog.Builder(Login.this).setIcon(
                            getResources().getDrawable(
                                    R.drawable.login_error_icon)).setTitle("登录错误")
                            .setMessage("账号或密码不能为空，\n请输入后再登录!").create().show();
                }else{
                    Toast.makeText(this,"登录失败",Toast.LENGTH_SHORT);
                }
                break;
            default:
                break;
        }
    }

    public Handler handler=new Handler() {
        public void handleMessage(Message msg)
        {
            switch (msg.what){
                case 1:
                    String response=(String)msg.obj;
                    if(response.equals("true")){
                        Intent intent = new Intent();
                        intent.setClass(Login.this,MainActivity.class);
                        startActivity(intent);
                        Toast.makeText(getApplication(), "登录成功", Toast.LENGTH_SHORT);

                    }else{
                        new AlertDialog.Builder(Login.this).setIcon(
                                getResources().getDrawable(
                                        R.drawable.login_error_icon)).setTitle("登录失败")
                                .setMessage("账号密码不正确。\n请检查后重新输入!").create().show();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    //执行登录
//    public void login_dologin(View view){
//        String uid = userid.getText().toString();
//        String passwd = password.getText().toString();
//        try{
//            if(!"".equals(userid.getText().toString())&&!"".equals(password.getText().toString())){ //判断账号密码
//                SendByHttpClient(uid,passwd);
//            }else if("".equals(userid.getText().toString())||"".equals(password.getText().toString())){
//                new AlertDialog.Builder(Login.this).setIcon(
//                        getResources().getDrawable(
//                                R.drawable.login_error_icon)).setTitle("登录错误").setMessage("账号或密码不能为空，\n请输入后再登录!").create().show();
//            }
//        }catch(Exception e){
//            e.printStackTrace();
//            Toast.makeText(this, "登录失败", Toast.LENGTH_SHORT);
//        }

    // 传递信息给服务端
//    public void SendByHttpClient(final String id,final String pw){
//
//        new Thread(new Runnable(){
//            @Override
//            public void run(){
//                try{
//                    HttpClient httpclient = new DefaultHttpClient();
//                    HttpPost httpPost = new HttpPost("http://192.168.101.1:8080/Pap/Login");
//                    List<NameValuePair> params=new ArrayList<NameValuePair>();
//                    params.add(new BasicNameValuePair("userid",id));
//                    params.add(new BasicNameValuePair("password",pw));
//                    final UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params,"utf-8");
//                    httpPost.setEntity(entity);
//                    HttpResponse httpResponse = httpclient.execute(httpPost);
//                    if(httpResponse.getStatusLine().getStatusCode()==200){
//                        HttpEntity entityl = httpResponse.getEntity();
//                        String response = EntityUtils.toString(entityl,"utf-8");
//                        Message message = new Message();
//                        message.what = SHOW_RESPONSE;
//                        message.obj=response;
//                        handler.sendMessage(message);
//                    }
//
//                }catch(Exception e){
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//
//    }
}
