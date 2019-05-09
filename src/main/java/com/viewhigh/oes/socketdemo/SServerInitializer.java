package com.viewhigh.oes.socketdemo;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

public class SServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        System.out .println("服务器----Initializer");
        ChannelPipeline pipeline = socketChannel.pipeline();

        pipeline.addLast(new LengthFieldBasedFrameDecoder(1024,0,4,0,4));
        pipeline.addLast(new LengthFieldPrepender(4));
        //字符串解码
        pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));
        //字符串编码
        pipeline.addLast(new StringEncoder(CharsetUtil.UTF_8));
        //自己定义的处理器
        pipeline.addLast(new SocketServerHandler());
    }
}
