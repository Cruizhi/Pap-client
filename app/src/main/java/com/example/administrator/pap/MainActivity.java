package com.example.administrator.pap;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MainActivity extends FragmentActivity implements OnClickListener {

    private LinearLayout shouye;
    private LinearLayout user;
    private LinearLayout fosterage;
    private LinearLayout cart;
    private LinearLayout chat;

    private ImageView iv_menu_0;
    private ImageView iv_menu_1;
    private ImageView iv_menu_2;
    private ImageView iv_menu_3;
    private ImageView iv_menu_4;

    private Fragment home_F;
    private Fragment chat_F;
    private Fragment fosterage_F;
    private Fragment cart_F;
    private Fragment user_F;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView(); //初始化View
        initEvents(); //绑定选项事件
        setSelect(0); //默认显示首页内容
    }

    private void initView(){
        shouye = (LinearLayout)findViewById(R.id.shouye);
        chat = (LinearLayout)findViewById(R.id.chat);
        fosterage = (LinearLayout)findViewById(R.id.fosterage);
        cart = (LinearLayout)findViewById(R.id.cart);
        user = (LinearLayout)findViewById(R.id.user);
        iv_menu_0 = (ImageView)findViewById(R.id.iv_menu_0);
        iv_menu_1 = (ImageView)findViewById(R.id.iv_menu_1);
        iv_menu_2 = (ImageView)findViewById(R.id.iv_menu_2);
        iv_menu_3 = (ImageView)findViewById(R.id.iv_menu_3);
        iv_menu_4 = (ImageView)findViewById(R.id.iv_menu_4);
    }

    private void initEvents(){
        shouye.setOnClickListener(this);
        chat.setOnClickListener(this);
        fosterage.setOnClickListener(this);
        cart.setOnClickListener(this);
        user.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        resetImage();
        switch(v.getId()){
            case R.id.shouye:
                setSelect(0);
                break;
            case R.id.chat:
                setSelect(1);
                break;
            case R.id.fosterage:
                setSelect(2);
                break;
            case R.id.cart:
                setSelect(3);
                break;
            case R.id.user:
                setSelect(4);
                break;
        }
    }

    //切换显示内容，将点击图标设置为亮
    private void setSelect(int i){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction(); //创建一个新事务
        hideFragment(transaction); //隐藏所有Fragment，然后再处理要显示的Fragment
        switch(i){
            case 0:
                if(home_F == null){
                    home_F = new Home_F();
                    transaction.add(R.id.show_layout,home_F);
                }else{
                    transaction.show(home_F);
                }
                iv_menu_0.setImageResource(R.drawable.guide_home_on);
                break;
            case 1:
                if(chat_F == null){
                    chat_F = new Chat_F();
                    transaction.add(R.id.show_layout,chat_F);
                }else{
                    transaction.show(chat_F);
                }
                iv_menu_1.setImageResource(R.drawable.guide_tfaccount_on);
                break;
            case 2:
                if(fosterage_F == null){
                    fosterage_F = new Fosterage_F();
                    transaction.add(R.id.show_layout,fosterage_F);
                }else{
                    transaction.show(fosterage_F);
                }
                iv_menu_2.setImageResource(R.drawable.guide_discover_on);
                break;
            case 3:
                if(cart_F == null){
                    cart_F = new Cart_F();
                    transaction.add(R.id.show_layout,cart_F);
                }else{
                    transaction.show(cart_F);
                }
                iv_menu_3.setImageResource(R.drawable.guide_cart_on);
                break;
            case 4:
                if(user_F == null){
                    user_F = new User_F();
                    transaction.add(R.id.show_layout,user_F);
                }else{
                    transaction.show(user_F);
                }
                iv_menu_4.setImageResource(R.drawable.guide_account_on);
                break;
            default:
                break;
        }
        transaction.commit();//提交事务
    }

    //隐藏所有Fragment
    private void hideFragment(FragmentTransaction transaction){
        if(home_F != null){
            transaction.hide(home_F);
        }
        if(chat_F != null){
            transaction.hide(chat_F);
        }
        if(fosterage_F != null){
            transaction.hide(fosterage_F);
        }
        if(cart_F != null){
            transaction.hide(cart_F);
        }
        if(user_F != null){
            transaction.hide(user_F);
        }
    }

    private void resetImage(){
        iv_menu_4.setImageResource(R.drawable.guide_account_nm);
        iv_menu_3.setImageResource(R.drawable.guide_cart_nm);
        iv_menu_2.setImageResource(R.drawable.guide_discover_nm);
        iv_menu_1.setImageResource(R.drawable.guide_tfaccount_nm);
        iv_menu_0.setImageResource(R.drawable.guide_home_nm);
    }

    //实现接口接受fragment传过来的值，实现onwindowfocusChanged方法，在fragment创建接口，调用方法
//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//
//        if(mFragment instanceOf IOnFocusListenable) {
//            ((IOnFocusListenable) mFragment).onWindowFocusChanged(hasFocus);
//        }
//    }
//    public interface IOnFocusListener {
//
//        public void onWindowFocusChanged(boolean hasFocus);
//    }

}
