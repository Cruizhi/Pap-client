package com.example.administrator.util;

import android.util.Log;

/**
 * Created by Administrator on 2017/9/18.
 */

public class L {
    private static boolean DE_BUG=true;
    private  static String TAG="crz";

    public static void i_crz(String text){
        if(DE_BUG)
        Log.i("crz",text);//e
    }
    public static void i_ch(String text){
        if(false)
            Log.i("ch",text);
    }
}
