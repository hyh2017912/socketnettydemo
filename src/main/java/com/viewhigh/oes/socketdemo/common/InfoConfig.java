package com.viewhigh.oes.socketdemo.common;

public enum InfoConfig {
    SERVER_PORT_01(1,58883,"服务端端口号"),
    SERVER_PORT_02(2,58884,"服务端端口号"),
    HEART_INTERVAL_TIME_SERVER(3,30,"服务端心跳时间间隔"),
    HEART_INTERVAL_TIME_CLIENT(3,120,"客户端心跳时间间隔"),
    HEART_INTERVAL_FREQUENCY(4,3,"心跳间隔次数"),

    SO_BACKLOG(5,32,"设置tcp缓冲区"),
    SO_SNDBUF(6,128,"设置发送缓冲区"),
    SO_RCVBUF(7,256,"设置接收缓冲区");

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public int getConfig() {
        return config;
    }

    public void setConfig(int config) {
        this.config = config;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    int no;
    int config;
    String  des;
    InfoConfig(int no, int config, String des){
        this.no = no;
        this.config = config;
        this.des = des;
    }
}
