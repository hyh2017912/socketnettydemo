package com.viewhigh.oes.socketdemo.common;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HeartCommon extends ChannelHandlerAdapter {
    private static Map<String,Integer>  heartTimes = new ConcurrentHashMap<>();
    public static void handleReaderIdle(ChannelHandlerContext ctx, String s) {

        if (heartTimes.get("id") == null){
            heartTimes.put("id",1);
            System.out.println("这里是handleReaderIdle：进行第" + heartTimes.get("id") + "次心跳");
        }else if (heartTimes.get("id") < 3){
            heartTimes.put("id",heartTimes.get("id") + 1);
            System.out.println("这里是handleReaderIdle：进行第" + heartTimes.get("id") + "次心跳");
        }else {
            System.out.println("这里是handleReaderIdle：进行第" + heartTimes.get("id") + "次心跳结束," + s + "将关闭无效连接:" + ctx.name());
            ctx.close();
            heartTimes.remove("id");
        }
    }

    public static void handleWriterIdle(ChannelHandlerContext ctx, String s) {
        System.out.println("这里是handleWriterIdle：连接30秒内无交互，" + s + "将关闭无效连接:" + ctx.name());
        ctx.close();
    }

    public static void handleAllIdle(ChannelHandlerContext ctx, String s) {
        System.out.println("这里是handleAllIdle：连接30秒内无交互，" + s + "将关闭无效连接:" + ctx.name());
        ctx.close();
    }

    public static void heartHandler(ChannelHandlerContext ctx, IdleStateEvent e, String s) throws IOException {
        switch (e.state()) {
            case READER_IDLE:
                handleReaderIdle(ctx,s);
                break;
            case WRITER_IDLE:
                handleWriterIdle(ctx,s);
                break;
            case ALL_IDLE:
                handleAllIdle(ctx,s);
                break;
            default:
                throw new IOException("未知的IdleStateEvent状态");
        }
    }
}
