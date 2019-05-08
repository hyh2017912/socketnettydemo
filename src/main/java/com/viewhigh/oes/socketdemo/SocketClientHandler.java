package com.viewhigh.oes.socketdemo;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.util.Scanner;

public class SocketClientHandler extends SimpleChannelInboundHandler<String> {
    private static Scanner inputScanner = new Scanner(System.in);
    @Override
    protected void messageReceived(ChannelHandlerContext ctx, String msg){
        //服务端的远程地址
//        System.out.println(ctx.channel().remoteAddress());
//        System.out.println("client output: "+msg);
//        ctx.writeAndFlush("from client: "+ LocalDateTime.now());
    }

    /**
     * 向服务端发送数据
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx){
        System.out.println("客户端与服务端通道-开启：" + ctx.channel().localAddress() + "channelActive");
        System.out.println("服务端连接成功..."); // 连接完成
//        String sendInfo = "Hello 这里是客户端  你好啊！";
        this.sendMsg(ctx);
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
//        ctx.write(msg);
//        ctx.flush();  // 也可以直接使用writeAndFlush()方法
        this.sendMsg(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
        System.out.println("异常退出:" + cause.getMessage());
    }

    public void sendMsg(ChannelHandlerContext ctx){
        System.out.println("请输入你要发送的信息，并按回车键确认发送");
        String sendInfo = inputScanner.nextLine();
        if ("exit".equalsIgnoreCase(sendInfo)){
            this.closed(ctx);
            return;
        }
        System.out.println("客户端准备发送的数据包：" + sendInfo);
        ctx.writeAndFlush(Unpooled.copiedBuffer(sendInfo, CharsetUtil.UTF_8)); // 必须有flush
    }

    public void closed(ChannelHandlerContext ctx){
        ctx.close();
    }
}
