package com.viewhigh.oes.socketdemo;

import com.viewhigh.oes.socketdemo.common.InfoConfig;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;

import java.nio.ByteOrder;

public class SServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        System.out .println("服务器----Initializer");
        ChannelPipeline pipeline = socketChannel.pipeline();
        // 心跳事件
        pipeline.addLast(new IdleStateHandler(InfoConfig.HEART_INTERVAL_TIME_SERVER.getConfig(),
                0,0));
        // 这里的解码器参数配置，两端保持一直,显式配置大端传输
        pipeline.addLast(new LengthFieldBasedFrameDecoder(ByteOrder.BIG_ENDIAN,
                1024,0,4,0,4,true));
        pipeline.addLast(new LengthFieldPrepender(4));
        //字符串解码
//        pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));
        //字符串编码
//        pipeline.addLast(new StringEncoder(CharsetUtil.UTF_8));
        //自己定义的处理器
        pipeline.addLast(new SocketServerHandler());
    }
}
