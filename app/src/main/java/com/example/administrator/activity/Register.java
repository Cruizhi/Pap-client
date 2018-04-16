package com.example.administrator.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.http.UploadByServlet;
import com.example.administrator.pap.R;

import org.apache.http.message.BasicNameValuePair;

/**
 * Created by Administrator on 2017/9/3.
 */

public class Register extends Activity implements View.OnClickListener{
    private EditText user;  //姓名编辑框
    private EditText uid;  //账号编辑框
    private EditText password;  //密码编辑框
    private EditText password1;  //确认密码编辑框
    private EditText address;  //地址编辑框
    private EditText phone;  //电话编辑框
    private EditText email;  //邮箱编辑框

    private String url = "Register";

    private Button register;  //注册按钮


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        initView(); //初始化控件
        register.setOnClickListener(this);
    }

    public void initView(){
        user = (EditText)findViewById(R.id.register_user_edit);
        uid = (EditText)findViewById(R.id.register_id_edit);
        password = (EditText)findViewById(R.id.register_password_edit);
        password1 = (EditText)findViewById(R.id.register_password_edit_makesure);
        address = (EditText)findViewById(R.id.register_address_edit);
        phone = (EditText)findViewById(R.id.register_phone_edit);
        email = (EditText)findViewById(R.id.register_email_edit);
        register = (Button)findViewById(R.id.register_btn);
    }

    //回退
    public void register_back(View view){
        this.finish();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.register_btn:
                String username = user.getText().toString();
                String id = uid.getText().toString();
                String passwd = password.getText().toString();
                String passwd1 = password1.getText().toString();
                if(!passwd.equals(passwd1)){
                    Toast.makeText(this, "两次密码不一致", Toast.LENGTH_SHORT).show();
                }else if("".equals(username) || "".equals(passwd)){
                    new AlertDialog.Builder(Register.this).setTitle("注册错误")
                            .setMessage("账号或密码不能为空，\n请输入后再注册!").create().show();
                }else{
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            BasicNameValuePair bnvp0 = new BasicNameValuePair("username",user.getText().toString());
                            BasicNameValuePair bnvp1 = new BasicNameValuePair("userid",uid.getText().toString());
                            BasicNameValuePair bnvp2 = new BasicNameValuePair("password",password.getText().toString());
                            BasicNameValuePair bnvp3 = new BasicNameValuePair("address",address.getText().toString());
                            BasicNameValuePair bnvp4 = new BasicNameValuePair("phone",phone.getText().toString());
                            BasicNameValuePair bnvp5 = new BasicNameValuePair("email",email.getText().toString());
                            String response = UploadByServlet.post(url,bnvp0,bnvp1,bnvp2,bnvp3,bnvp4,bnvp5);
                            Message msg = new Message();
                            msg.obj = response;
                            msg.what = 1;
                            handler.sendMessage(msg);
                        }
                    }).start();
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
                        Toast.makeText(getApplication(), "注册成功", Toast.LENGTH_SHORT);
                        Intent intent = new Intent();
                        intent.setClass(Register.this,Login.class);
                        startActivity(intent);
                        Register.this.finish();
                    }else{
                        Toast.makeText(getApplication(), "注册失败", Toast.LENGTH_SHORT);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    //注册后执行登录
//    public void register_doregister(View view){
//        String username = user.getText().toString();
//        String id = uid.getText().toString();
//        String passwd = password.getText().toString();
//        String passwd1 = password1.getText().toString();
//        try{
//            if(!passwd.equals(passwd1)){
//                Toast.makeText(getApplicationContext(), "两次密码不一致", Toast.LENGTH_SHORT).show();
//            }else if("".equals(user.getText().toString()) ||
//                    "".equals(password.getText().toString())){
//                new AlertDialog.Builder(Register.this).setTitle("注册错误").setMessage("账号或密码不能为空，\n请输入后再注册!").create().show();
//            }else{
//                SendByHttpClient(username,id,passwd);
//            }
//        }catch(Exception e){
//            e.printStackTrace();
//            Toast.makeText(getApplication(), "注册失败", Toast.LENGTH_SHORT).show();
//        }
//    }
//
// 传递信息给服务端
//    public void SendByHttpClient(final String username,final String id,final String pw){
//
//        new Thread(new Runnable(){
//            @Override
//            public void run(){
//                try{
//                    HttpClient httpclient = new DefaultHttpClient();
//                    HttpPost httpPost = new HttpPost("http://192.168.101.1:8080/Pap/Register");
//                    List<NameValuePair> params=new ArrayList<NameValuePair>();
//                    params.add(new BasicNameValuePair("username",username));
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
