package com.example.administrator.pap;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.adapter.CartAdapter;
import com.example.administrator.bean.Cart;
import com.example.administrator.http.ConnectWeb;
import com.example.administrator.http.UploadByServlet;
import com.example.administrator.util.Constant;
import com.example.administrator.util.L;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/1.
 */

public class Cart_F extends Fragment {

    private ListView listview;
    private TextView AllPrice;
    private TextView Buy;
    private List<Cart> cartList = new ArrayList<Cart>();
    private List<Integer> listitemID = new ArrayList<Integer>();
    private CartAdapter myAdapter;
    private CheckBox AllCart;
    private boolean[] is_choice;

    private String url = "Cart";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //引入布局
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.viewcart, null);
        initView(view);
        return view;
    }

    private void initView(View view){
        listview = (ListView)view.findViewById(R.id.lv_cart_listview);
        AllCart = (CheckBox)view.findViewById(R.id.cb_cart_all);
        AllPrice = (TextView)view.findViewById(R.id.tv_cart_allprice);
        Buy = (TextView)view.findViewById(R.id.tv_cart_buy);
        is_choice = new boolean[cartList.size()];

        //获取购物车列表
        new Thread(new Runnable() {
            @Override
            public void run() {
                getCartList();
            }
        }).start();

        AllCart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //记录列表每一行选中状态的数量
                int isChoice_all = 0;
                if(isChecked){
                    //设置全选
                    for(int i = 0;i<cartList.size(); i++){
                        //如果选中全选，那么将列表每一行都选中
                        ((CheckBox)(listview.getChildAt(i)).findViewById(R.id.cb_cart_choose)).setChecked(true);
                    }
                }else{
                    //设置全取消
                    for(int i = 0;i<cartList.size(); i++){
                        //判断列表每一行是否处于选中状态，如果是，则计数+1
                        if(((CheckBox)(listview.getChildAt(i)).findViewById(R.id.cb_cart_choose)).isChecked()){
                            isChoice_all += 1;
                        }
                    }
                    //判断列表选中数量是否等于列表总数，如果等于，那么就需要执行全部取消
                    if(isChoice_all == cartList.size()){
                        //如果没有选中全部，那就将每一行都不选
                        for(int i = 0;i<cartList.size(); i++){
                            ((CheckBox)(listview.getChildAt(i)).findViewById(R.id.cb_cart_choose)).setChecked(false);
                        }
                    }
                }
            }
        });



//        AllPrice.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                listitemID.clear();
//                for(int i = 0;i<myAdapter.mChecked.size();i++){
//                    if(myAdapter.mChecked.get(i)){
//                        listitemID.add(i);
//                    }
//                }
//                if(listitemID.size()==0){
//                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
//                    builder1.setMessage("没有选中任何记录");
//                    builder1.show();
//                }else{
//                    StringBuilder sb = new StringBuilder();
//
//                    for(int i=0;i<listitemID.size();i++){
//                        sb.append("ItemID="+listitemID.get(i)+" . ");
//                    }
//                    AlertDialog.Builder builder2 = new AlertDialog.Builder(getActivity());
//                    builder2.setMessage(sb.toString());
//                    builder2.show();
//                }
//            }
//        });

    }

    /** adapter的回调函数，当点击CheckBox的时候传递点击位置和checkBox的状态 */
//    @Override
//    public void getChoiceData(int position, boolean isChoice){
//        double allprice = 0;
//        //得到点击哪一行
//        if(isChoice){
//            if(cartList.size() != 0){
//                allprice += Double.valueOf(cartList.get(position).getPrice());
//            }
//        }else{
//            if(cartList.size() != 0){
//                allprice -= Double.valueOf(cartList.get(position).getPrice());
//            }
//        }
//        //记录列表处于选中状态的数量
//        int numchoice = 0;
//        for(int i = 0;i<cartList.size(); i++){
//            //判断列表中每一行的选中状态，选中加1
//            if(listview.getChildAt(i) != null && ((CheckBox)(listview.getChildAt(i)).findViewById(R.id.cb_cart_choose)).isChecked()){
//                //列表处于选中的数量+1
//                numchoice += 1;
//                is_choice[i]=true;
//            }
//        }
//        //判断列表中CHeckBox是否全部选中
//        if(numchoice == cartList.size()){
//            AllCart.setChecked(true);
//        }else{
//            AllCart.setChecked(false);
//        }
//        AllPrice.setText("合计:￥"+allprice+"");
//    }

    private void getCartList(){
        new Thread(){
            public void run(){
                try{
                    BasicNameValuePair bnvp = new BasicNameValuePair("userid", Constant.user.getUserid());
                    String response = UploadByServlet.post(url,bnvp);
                    cartList = new ConnectWeb().getCartList(response);
                    Message m =new Message();
                    m.obj = cartList;
                    handler.sendMessage(m);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }.start();
    }

    Handler handler = new Handler(){
        public void handleMessage(Message msg){
            if(cartList == null){
                return;
            }
            L.i_crz("Cart_F -- Handle"+cartList.size());
            myAdapter = new CartAdapter(getActivity(),cartList,R.layout.item_cart);
            //myAdapter.setOnCheckedChanged(this);
            listview.setAdapter(myAdapter);

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    double allprice = 0;
                    listitemID.clear();
                    for(int i = 0;i<myAdapter.mChecked.size(); i++){
                        if(myAdapter.mChecked.get(i)){
                            listitemID.add(i);
                        }
                    }
                    for(int i = 0; i < listitemID.size();i++){
                        allprice += cartList.get(listitemID.get(i)).getPrice();
                        L.i_crz("Cart_F -- allprice:..."+ allprice);
                    }
                    AllPrice.setText("合计:￥"+allprice+"");
                    Buy.setText("结算("+listitemID.size()+")");
                }
            });
        }
    };
}
