package com.viewhigh.oes.socketdemo.common;

import com.viewhigh.oes.socketdemo.SocketClient;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.util.Timeout;

import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

/**
 * 监听连接是否成功
 */
public class ConnectionListener implements ChannelFutureListener {
    private SocketClient socketClient = new SocketClient();
    @Override
    public void operationComplete(ChannelFuture future) throws Exception {
        if (!future.isSuccess()){
            final EventLoop loop = future.channel().eventLoop();
            loop.schedule(() -> {
                System.out.println("监听到：服务连接失败，重新连接......");
                try {
                    socketClient.initSocketClient();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            },2,TimeUnit.SECONDS);
        }else{
            System.out.println("监听到：服务连接成功......");
        }
    }
}
