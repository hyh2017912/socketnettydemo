package com.viewhigh.oes.socketdemo;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;

import java.io.IOException;

public class HeartCommon extends ChannelHandlerAdapter {
    public static void sendPingMsg(ChannelHandlerContext context){

    }

    public static void sendPongMsg(ChannelHandlerContext context){

    }

    public static void handleReaderIdle(ChannelHandlerContext ctx) {
        System.out.println("这里是handleReaderIdle");
    }

    public static void handleWriterIdle(ChannelHandlerContext ctx) {
        System.out.println("这里是handleWriterIdle");
    }

    public static void handleAllIdle(ChannelHandlerContext ctx) {
        System.out.println("这里是handleAllIdle");
    }

    public static void heartHandler(ChannelHandlerContext ctx, IdleStateEvent e) throws IOException {
        switch (e.state()) {
            case READER_IDLE:
                handleReaderIdle(ctx);
                break;
            case WRITER_IDLE:
                handleWriterIdle(ctx);
                break;
            case ALL_IDLE:
                handleAllIdle(ctx);
                break;
            default:
                throw new IOException("未知的IdleStateEvent状态");
        }
    }
}
