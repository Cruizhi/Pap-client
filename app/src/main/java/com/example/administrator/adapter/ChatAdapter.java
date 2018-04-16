package com.example.administrator.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.bean.Chat;
import com.example.administrator.pap.R;

import java.util.List;

/**
 * Created by Administrator on 2017/10/10.
 */

public class ChatAdapter extends BaseAdapter {
    private List<Chat> coll;
    private LayoutInflater mInflater;
    public static interface IMsgViewType
    {
        int IMVT_COM_MSG = 0;
        int IMVT_TO_MSG = 1;
    }

    public ChatAdapter(Context context, List<Chat> coll) {
        this.coll = coll;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return coll.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return coll.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
    public int getItemViewType(int position) {
        // TODO Auto-generated method stub
        Chat entity = coll.get(position);

        if (entity.getMsgType())
        {
            return IMsgViewType.IMVT_COM_MSG;
        }else{
            return IMsgViewType.IMVT_TO_MSG;
        }

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Chat entity = coll.get(position);
        boolean isComMsg = entity.getMsgType();
        ViewHolder viewHolder = null;
        if (convertView == null)
        {
            if (isComMsg)
            {
                convertView = mInflater.inflate(R.layout.item_chat_left, null);
            }else{
                convertView = mInflater.inflate(R.layout.item_chat_right, null);
            }

            viewHolder = new ViewHolder();
            viewHolder.tvSendTime = (TextView) convertView.findViewById(R.id.tv_chat_sendtime);
            viewHolder.tvUserName = (TextView) convertView.findViewById(R.id.tv_chat_username);
            viewHolder.tvContent = (TextView) convertView.findViewById(R.id.tv_chat_chatcontent);
            viewHolder.isComMsg = isComMsg;

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvSendTime.setText(entity.getDate());
        viewHolder.tvUserName.setText(entity.getName());
        viewHolder.tvContent.setText(entity.getText());

        return convertView;
    }
    static class ViewHolder {
        public TextView tvSendTime;
        public TextView tvUserName;
        public TextView tvContent;
        public boolean isComMsg = true;
    }
}
