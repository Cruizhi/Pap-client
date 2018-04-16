package com.example.administrator.pap;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.activity.GoodsDetailView;
import com.example.administrator.activity.WareActivity;
import com.example.administrator.adapter.GoodsAdapter;
import com.example.administrator.bean.Goods;
import com.example.administrator.http.ConnectWeb;
import com.example.administrator.util.L;

import java.io.Serializable;
import java.util.List;

import static com.example.administrator.pap.R.layout.item_shouye;

/**
 * Created by Administrator on 2017/9/4.
 */

public class Home_F extends Fragment {

    private TextView tv_top_title;  //搜索按钮
    private ListView ListView;  //声明listview类型变量
    private ProgressDialog myDialog;
    private List<Goods> goodsList;
    private GoodsAdapter myAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //引入布局
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.viewshouye, null);
        initView(view);
        L.i_crz("数据集-onCreateView");
        return view;
    }

    private void initView(View view){//igos
        L.i_crz("数据集-initView");
        ListView = (ListView)view.findViewById(R.id.listview);
        tv_top_title = (TextView)view.findViewById(R.id.tv_top_title);
        tv_top_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                //跳转到宝贝搜索界面
                Intent intent=new Intent(getActivity(),WareActivity.class);
                startActivity(intent);
            }
        });


        myDialog = ProgressDialog.show(getActivity(), "请稍等...", "数据检索中...",true);
        new Thread(new Runnable() {
            @Override
            public void run() {

                getGoodsList(); //读取商品列表***************************************
            }
        }).start();
        ListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                Goods theGood = goodsList.get(position);  //获取列表当前点中的商品
                Intent intent = new Intent();
                Bundle bundle = new Bundle();  //创建Bundle对象
                intent.setClass(getActivity(),GoodsDetailView.class);
                bundle.putSerializable("GoodObj", (Serializable) theGood);
                intent.putExtras(bundle);
                startActivity(intent);
            }

        });
    }

    private void getGoodsList(){
        new Thread(){
            public void run(){
                try{
                    L.i_crz("数据集-getGoodsList");
                    goodsList = new ConnectWeb().getList();
                    Message m = new Message();
                    m.obj = goodsList;
                    handler.sendMessage(m);
                }catch(Exception e){
                    e.printStackTrace();
                }finally{
                   myDialog.dismiss();
                }
            }
        }.start();
    }

    Handler handler = new Handler(){
        public void handleMessage(Message msg){
            if(goodsList == null){
                L.i_crz("Home_F -- 数据集为空");
                return;
            }
            L.i_crz("Home_F -- Handle");
            myAdapter = new GoodsAdapter(getActivity(), goodsList, item_shouye);
            ListView.setAdapter(myAdapter);
        }
    };

    //读取商品列表数据
//    private void getGoodsList(){
//
//        L.i_crz("数据集-getGoodsList");
//        new Thread(){
//            public void run(){
//                try{
//                    goodsList = new ConnectWeb().getList();  //获取推荐商品列表
//                    Message m= new Message();
//                    m.obj = goodsList;
//                    listHandler.sendMessage(m);
//                }catch(Exception e){
//                    e.printStackTrace();
//                }finally{
//                    myDialog.dismiss();
//                }
//            }
//        }.start();
//
//    }
//
//    Handler listHandler = new Handler(){
//        public void handleMessage(Message msg){
//            if(goodsList.size()==0){
//                return ;
//            }
//            showGoodsList();
//        }
//    };
//
//    //商品填充适配器
//    public void showGoodsList() {
//        L.i_crz("数据集-showGoodsList");
//        SimpleAdapter adapter = new SimpleAdapter(getActivity(), getTripList(),
//                R.layout.item_shouye, new String[] { "img", "title", "price"
//        }, new int[] { R.id.img, R.id.title,
//                R.id.price });
//        ListView.setAdapter(adapter); //为tripListView添加适配器adapter
//        adapter.setViewBinder(new SimpleAdapter.ViewBinder() {
//            public boolean setViewValue(View arg0, Object arg1,
//                                        String textRepresentation) {
//                if ((arg0 instanceof ImageView) & (arg1 instanceof Bitmap)) {
//                    ImageView imageView = (ImageView) arg0;
//                    Bitmap bitmap = (Bitmap) arg1;
//                    imageView.setImageBitmap(bitmap);
//                    return true;
//                } else {
//                    return false;
//                }
//
//            }
//        });
//    }
//
//    public List<Map<String, Object>> getTripList() {
//
//        String url = "http://192.168.101.1:8080/Pap/";
//        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
//        for (int i = 0; i < goodsList.size(); i++) {
//            Map<String, Object> map = new HashMap<String, Object>();
//            Goods goods = goodsList.get(i);
//            Log.i("ch","数据集测试："+url+goods.getDir()+goods.getPic());
//            try {
//                URL picUrl = new URL(url+goods.getDir()+goods.getPic());
//                Bitmap pngBM = BitmapFactory.decodeStream(picUrl.openStream());
//                map.put("img", pngBM);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            map.put("title", "商品名称："+goods.getBrand());
//            map.put("price", "商品价格："+"￥" + goods.getPrice());
//
//            list.add(map);
//        }
//
//        return list;
//    }


}
