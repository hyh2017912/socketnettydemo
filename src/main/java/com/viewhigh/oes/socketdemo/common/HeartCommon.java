package com.viewhigh.oes.socketdemo.common;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HeartCommon extends ChannelHandlerAdapter {
//    private static Map<String,Integer>  heartTimes = new ConcurrentHashMap<>();
    private static ThreadLocal<Integer> tl = new ThreadLocal<Integer>(){
    @Override
    protected Integer initialValue() {
        return 0;
    }
};
    public static void handleReaderIdle(ChannelHandlerContext ctx, String s) {
        if (tl.get() == 0){
            tl.set(1);
            System.out.println("这里是handleReaderIdle：进行第" + tl.get() + "次心跳");
        }else if (tl.get() < InfoConfig.HEART_INTERVAL_FREQUENCY.getConfig() - 1){
            tl.set(tl.get() + 1);
            System.out.println("这里是handleReaderIdle：进行第" + tl.get() + "次心跳");
        }else {
            System.out.println("这里是handleReaderIdle：进行第" + (tl.get() + 1) + "次心跳结束," + s + "将关闭无效连接:" + ctx.name());
            ctx.close();
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
