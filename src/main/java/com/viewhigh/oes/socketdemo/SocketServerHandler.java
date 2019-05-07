package com.viewhigh.oes.socketdemo;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.UUID;

public class SocketServerHandler extends SimpleChannelInboundHandler {

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {
        //打印出客户端地址
        System.out.println(ctx.channel().remoteAddress()+", "+msg);
        ctx.channel().writeAndFlush("form server: "+ UUID.randomUUID());
    }

}
