package com.example.myapplication;

public class PushHistory {

    private int iv_icon;
    private String tv_send_time;
    private String tv_send_msg;
    private String tv_push_type;

    public PushHistory(int iv_icon, String tv_push_type, String tv_send_time, String tv_send_msg) {
        //this.iv_icon = iv_icon;
        this.tv_send_time = tv_send_time;
        this.tv_send_msg = tv_send_msg;
        this.tv_push_type = tv_push_type;
    }

    public int getIv_icon() {
        return iv_icon;
    }

    public void setIv_icon(int iv_icon) {
        this.iv_icon = iv_icon;
    }

    public String getTv_send_time() {
        return tv_send_time;
    }

    public void setTv_send_time(String tv_send_time) {
        this.tv_send_time = tv_send_time;
    }

    public String getTv_send_msg() {
        return tv_send_msg;
    }

    public void setTv_send_msg(String tv_send_msg) {
        this.tv_send_msg = tv_send_msg;
    }

    public String getTv_push_type() {
        return tv_push_type;
    }

    public void setTv_push_type(String tv_push_type) {
        this.tv_push_type = tv_push_type;
    }
}
