package com.example.administrator.http;

import com.example.administrator.bean.Cart;
import com.example.administrator.bean.Fosterage;
import com.example.administrator.bean.Goods;
import com.example.administrator.bean.User;
import com.example.administrator.util.L;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/3.
 */

public class ConnectWeb {
    public static String Url = "http://192.168.101.1:8080/Pap/";

    //访问网站数据库获取数据(商品)
    public String getByServlet(String url){
        String str = "";
        try{
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost request = new HttpPost(Url+url);
            HttpResponse response = httpClient.execute(request);
            if(response.getStatusLine().getStatusCode()==200){
                HttpEntity entity = response.getEntity();
                str = EntityUtils.toString(entity,"utf-8");
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        return str;
    }

    //获取商品列表
    public List<Goods> getList(){
        List<Goods> mylist = new ArrayList();
        String response = getByServlet("Goods"); //获取list
        try{
       //JSONArray ja = JSONArray.fromObject(response);
        JSONArray ja = new JSONArray(response);
        for(int i = 0;i < ja.length(); i++) {
            JSONObject jo = ja.getJSONObject(i);
            Goods goods = new Goods();
            goods.setBrand(jo.getString("brand"));
            goods.setBcount(jo.getInt("bcount"));
            goods.setPrice(jo.getDouble("price"));
            goods.setDes(jo.getString("des"));
            goods.setDir(jo.getString("dir"));
            goods.setPic(jo.getString("pic"));
            goods.setUid(jo.getInt("uid"));
            mylist.add(goods);
        }
        }catch(Exception e){
            e.printStackTrace();
        }
        return mylist;
    }

    //获取寄养信息
    public List<Fosterage> getFostList(String url){
        List<Fosterage> mylist = new ArrayList();
        String response = getByServlet(url); //获取list
        try{
            JSONArray ja = new JSONArray(response);
            for(int i = 0;i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                Fosterage fosterage = new Fosterage();
                fosterage.setId(jo.getInt("id"));
                fosterage.setTitle(jo.getString("title"));
                fosterage.setDes(jo.getString("des"));
                fosterage.setPrice(jo.getDouble("price"));
                fosterage.setDic(jo.getString("dic"));
                fosterage.setPic(jo.getString("pic"));
                fosterage.setAddress(jo.getString("address"));
                fosterage.setDay(jo.getString("day"));
                fosterage.setDate(jo.getString("date"));
                fosterage.setUid(jo.getString("uid"));
                mylist.add(fosterage);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return mylist;
    }

    //获取购物车列表
    public List<Cart> getCartList(String response){
        List<Cart> mylist = new ArrayList<Cart>();
        try{
            JSONArray ja = new JSONArray(response);
            for(int i = 0;i < ja.length();i++){
                JSONObject jo = ja.getJSONObject(i);
                Cart cart = new Cart();
                cart.setUserid(jo.getString("userid"));
                cart.setBuyid(jo.getString("buyid"));
                cart.setBrand(jo.getString("brand"));
                cart.setDes(jo.getString("des"));
                cart.setPrice(jo.getDouble("price"));
                cart.setBcount(jo.getInt("bcount"));
                cart.setDir(jo.getString("dir"));
                cart.setPic(jo.getString("pic"));
                mylist.add(cart);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return mylist;
    }

    //获取当前登录的用户信息
    public static User getUserinformation(String response){
        User user = new User();
        try {
            JSONArray ja = new JSONArray(response);
            JSONObject jo = ja.getJSONObject(0);
            user.setUserid(jo.getString("userid"));
            user.setPassword(jo.getString("password"));
            user.setAddress(jo.getString("address"));
            user.setEmail(jo.getString("email"));
            user.setPhone(jo.getString("phone"));
            user.setUsername(jo.getString("username"));
            L.i_crz("获取当前登录的用户信息--"+user.getUserid()+user.getUsername()+user.getAddress());
        }catch(Exception e){
            e.printStackTrace();
        }
        return user;
    }

    //上传图片
    public static void uploadFile(String imagepath,String imagename){
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        try{
            URL url = new URL(Url+"UploadServlet");
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            //允许Input,Output,不使用Cache
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
            //设置传送的method = POST
            con.setRequestMethod("POST");
            con.setRequestProperty("Connection","Keep-Alive");
            con.setRequestProperty("Charset","UTF-8");
            con.setRequestProperty("Content-Type","multipart/form-data;boundary="+boundary);
            //设置DataOutputStream
            DataOutputStream ds = new DataOutputStream(con.getOutputStream());
            ds.writeBytes(twoHyphens+boundary+end);
            ds.writeBytes("Content-Disposition:form-data;"+"name=\"file\";filename=\""+imagename+"\""+end);
            ds.writeBytes(end);
            //获取文件的FileInputStream
            FileInputStream fStream = new FileInputStream(imagepath);
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int length = -1;
            while((length = fStream.read(buffer)) != -1){
                //从文件读取数据到缓冲区
                ds.write(buffer,0,length);
            }
            ds.writeBytes(end);
            ds.writeBytes(twoHyphens + boundary +twoHyphens +end);
            fStream.close();
            ds.flush();;
            InputStream is = con.getInputStream();
            int ch;
            StringBuffer b =new StringBuffer();
            while((ch = is.read()) != -1){
                b.append((char)ch);
            }
            //将数据显示于Dialog
            System.out.println("上传成功");
            ds.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
