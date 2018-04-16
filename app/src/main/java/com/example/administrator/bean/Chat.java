package com.example.administrator.bean;

/**
 * Created by Administrator on 2017/10/10.
 */

public class Chat {
    private String name;
    private String date;
    private String text;
    private boolean isComMeg = false;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public boolean getMsgType() {
        return isComMeg;
    }
    public void setMsgType(boolean isComMsg) {
        isComMeg = isComMsg;
    }
    public Chat() {
    }
    public Chat(String name, String date, String text, boolean isComMsg) {
        super();
        this.name = name;
        this.date = date;
        this.text = text;
        this.isComMeg = isComMsg;
    }
}
