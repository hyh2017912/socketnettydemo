package com.viewhigh.oes.socketdemo;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

public class SocketClient {

    /**
     * 初始化一个客户端应用
     */
    public void initSocketClient() throws UnknownHostException, InterruptedException {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            Bootstrap bs = new Bootstrap();
            bs.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(InetAddress.getLocalHost(), 55882))
                    .handler(new SClientInitializer());
            ChannelFuture cf = bs.connect().sync(); // 异步连接服务器
            System.out.println("服务端连接成功..."); // 连接完成
            cf.channel().closeFuture().sync(); // 异步等待关闭连接channel
            System.out.println("连接已关闭.."); // 关闭完成
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            eventLoopGroup.shutdownGracefully().sync(); // 释放线程池资源
        }

    }
}
