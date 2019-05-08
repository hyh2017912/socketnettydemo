package com.viewhigh.oes.socketdemo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class SocketServer {

    /**
     * 初始化一个服务端应用
     */
    public void initSocketServer() throws InterruptedException {
        //创建两个线程组，一个通常称为boss，一个通常称为worker
        // boss用于接收连接，并将接收的连接及时注册到worker
        // worker用于和客户端交互，处理这些连接
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try{
            ServerBootstrap sb = new ServerBootstrap();
            sb.group(bossGroup, workerGroup) // 绑定两个线程组
                    .channel(NioServerSocketChannel.class) //指定nio模式
                    .localAddress(55882) // 端口可以不在这里绑定,后面的绑定会覆盖这里
                    .option(ChannelOption.SO_BACKLOG, 1024) // 设置tcp缓冲区
                    .option(ChannelOption.SO_SNDBUF, 32 * 1024) //设置发送缓冲区
                    .option(ChannelOption.SO_RCVBUF, 32 * 1024) //设置接收缓冲区
                    .option(ChannelOption.SO_KEEPALIVE, true) //保持连接
                    //在服务器端的handler()方法表示对bossGroup起作用，而childHandler表示对wokerGroup起作用
                    .childHandler(new SServerInitializer());
            ChannelFuture cf = sb.bind(55883).sync(); // todo 服务器异步创建绑定,也可以在这里绑定端口,可以绑定多个端口
            ChannelFuture cf1 = sb.bind(55884).sync();
            System.out.println(SocketServer.class + " 启动正在监听： " + cf.channel().localAddress());
            cf.channel().closeFuture().sync(); // 关闭服务器通道
            System.out .println("服务器通道已关闭！");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully().sync(); // 释放线程池资源
            bossGroup.shutdownGracefully().sync();
            System.out .println("服务器资源已关闭！");
        }

    }
}
