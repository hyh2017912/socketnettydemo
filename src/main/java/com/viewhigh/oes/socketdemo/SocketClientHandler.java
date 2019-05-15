package com.viewhigh.oes.socketdemo;

import com.viewhigh.oes.socketdemo.utils.SockerUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.EventLoop;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class SocketClientHandler extends SimpleChannelInboundHandler<String> {
    private int readerIdleTime = 1;
    private int writerIdleTime = 1;
    private int allIdleTime = 1;
    private SocketClient socketClient = new SocketClient();
    private static Scanner inputScanner = new Scanner(System.in);
    @Override
    protected void messageReceived(ChannelHandlerContext ctx, String msg){
        //服务端的远程地址
//        System.out.println(ctx.channel().remoteAddress());
//        System.out.println("sclient output: "+msg);
//        ctx.writeAndFlush("from sclient: "+ LocalDateTime.now());
    }

    /**
     * 向服务端发送数据，发送string
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx){
        System.out.println("客户端与服务端通道-开启：" + ctx.channel().localAddress() + "channelActive");
        System.out.println("服务端连接成功..."); // 连接完成
        new Thread(() -> SockerUtils.sendMsg(ctx,"客户端首次")).start();
    }

    /**
     * channelInactive
     * channel 通道 Inactive 不活跃的
     * 当客户端主动断开服务端的链接后，这个通道就是不活跃的。也就是说客户端与服务端的关闭了通信通道并且不可以传输数据
     *
     */
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端与服务端通道-关闭：" + ctx.channel().localAddress() + "channelInactive");

        // todo 使用过程中断线重连
        final EventLoop eventLoop = ctx.channel().eventLoop();
        System.out.println("开始掉线重连01");
        eventLoop.schedule(new Runnable() {
            @Override
            public void run() {
                System.out.println("开始掉线重连02");
                try {
                    socketClient.initSocketClient();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 2, TimeUnit.SECONDS);
        super.channelInactive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.println("读取通道信息..");
        ByteBuf byteBuf = (ByteBuf)msg;
        ByteBuf buf = byteBuf.readBytes(byteBuf.readableBytes());

        System.out.println("客户端接收到服务端信息：" + buf.toString(Charset.forName("utf-8")));
//        ctx.write(msg);
//        ctx.flush();  // 也可以直接使用writeAndFlush()方法
        SockerUtils.sendMsg(ctx,"客户端重复");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
        System.out.println("异常退出:" + cause.getMessage());
    }

/*    public void sendMsg(ChannelHandlerContext ctx){
        System.out.println("请输入你要发送的信息，并按回车键确认发送");
        String sendInfo = inputScanner.nextLine();
        if ("exit".equalsIgnoreCase(sendInfo)){
            SockerUtils.closed(ctx);
            return;
        }
        System.out.println("客户端准备发送的数据包：" + sendInfo);
        ctx.writeAndFlush(Unpooled.copiedBuffer(sendInfo, CharsetUtil.UTF_8)); // 必须有flush
    }*/

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        ChannelId id = ctx.channel().id();
        if (evt instanceof IdleStateEvent){
            IdleStateEvent e = (IdleStateEvent) evt;
            switch (e.state()) {
                case READER_IDLE:
                    System.out.println("通道0x" + id + "读空闲第" + readerIdleTime ++ + "心跳");
                    break;
                case WRITER_IDLE:
                    System.out.println("通道0x" + id + "写空闲第" + writerIdleTime ++ + "心跳");
                    break;
                case ALL_IDLE:
                    System.out.println("通道0x" + id + "读写空闲第" + allIdleTime ++ + "心跳");
                    break;
                default:
                    throw new IOException("未知的IdleStateEvent状态");
            }
        }else{
            super.userEventTriggered(ctx,evt);
        }
        if (readerIdleTime > 3){
            System.out.println(" [client]读空闲超过3次，关闭连接：" + id);
            ctx.close();
        }
        if (readerIdleTime > 3){
            System.out.println(" [client]写空闲超过3次，关闭连接：" + id);
            ctx.close();
        }
        if (readerIdleTime > 3){
            System.out.println(" [client]读写空闲超过3次，关闭连接：" + id);
            ctx.close();
        }
    }
}
