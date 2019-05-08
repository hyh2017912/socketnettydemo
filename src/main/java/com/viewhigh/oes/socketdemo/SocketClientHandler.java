package com.viewhigh.oes.socketdemo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;
import java.time.LocalDateTime;

public class SocketClientHandler extends SimpleChannelInboundHandler<String> {
    @Override
    protected void messageReceived(ChannelHandlerContext ctx, String msg){
        //服务端的远程地址
        System.out.println(ctx.channel().remoteAddress());
        System.out.println("client output: "+msg);
        ctx.writeAndFlush("from client: "+ LocalDateTime.now());
    }

    /**
     * 向服务端发送数据
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx){
        System.out.println("客户端与服务端通道-开启：" + ctx.channel().localAddress() + "channelActive");

        String sendInfo = "Hello 这里是客户端  你好啊！";
        System.out.println("客户端准备发送的数据包：" + sendInfo);
        ctx.writeAndFlush(Unpooled.copiedBuffer(sendInfo, CharsetUtil.UTF_8)); // 必须有flush

    }

    /**
     * channelInactive
     *
     * channel 通道 Inactive 不活跃的
     *
     * 当客户端主动断开服务端的链接后，这个通道就是不活跃的。也就是说客户端与服务端的关闭了通信通道并且不可以传输数据
     *
     */
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("客户端与服务端通道-关闭：" + ctx.channel().localAddress() + "channelInactive");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.println("读取通道信息..");
//        ByteBuf buf = msg.readBytes(msg.readableBytes());
//        ByteBuf buf = ((ByteBuf)msg).readBytes(((ByteBuf)msg).readableBytes());
//        System.out.println(
//                "客户端接收到的服务端信息:" + ByteBufUtil.hexDump(buf) + "; 数据包为:" + buf.toString(Charset.forName("utf-8")));
        System.out.println("客户端接收到服务端信息：" + msg);
    }

//    @Override
//    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        ctx.close();
//        System.out.println("异常退出:" + cause.getMessage());
//    }
}
