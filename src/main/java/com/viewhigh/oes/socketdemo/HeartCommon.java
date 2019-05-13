package com.viewhigh.oes.socketdemo;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;

import java.io.IOException;

public class HeartCommon extends ChannelHandlerAdapter {

    public static void handleReaderIdle(ChannelHandlerContext ctx, String s) {
        System.out.println("这里是handleReaderIdle：连接30秒内无交互，" + s + "将关闭无效连接:" + ctx.name());
        ctx.close();
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
