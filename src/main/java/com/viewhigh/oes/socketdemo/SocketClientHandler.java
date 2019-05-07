package com.viewhigh.oes.socketdemo;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.time.LocalDateTime;

public class SocketClientHandler extends SimpleChannelInboundHandler<String> {
    @Override
    protected void messageReceived(ChannelHandlerContext ctx, String msg) throws Exception {
        //服务端的远程地址
        System.out.println(ctx.channel().remoteAddress());
        System.out.println("client output: "+msg);
        ctx.writeAndFlush("from client: "+ LocalDateTime.now());
    }
}
