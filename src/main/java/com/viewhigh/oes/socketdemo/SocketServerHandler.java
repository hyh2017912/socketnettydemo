package com.viewhigh.oes.socketdemo;

import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;

import java.io.IOException;

public class SocketServerHandler extends SimpleChannelInboundHandler {
    private int readerIdleTime = 1;
    private int writerIdleTime = 1;
    private int allIdleTime = 1;
    private String channelId;

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Object msg){
    }

    /*
     * channel 通道 action 活跃的
     * 当客户端主动链接服务端的链接后，这个通道就是活跃的了。也就是客户端与服务端建立了通信通道并且可以传输数据
     */
    public void channelActive(ChannelHandlerContext ctx){
        channelId = "0x" + ctx.channel().id();
        System.out.println(ctx.channel().localAddress().toString() + " 通道" + channelId + "已激活！");
//        new Thread(() -> SockerUtils.sendMsg(ctx,"服务端首次")).start();
    }

    /*
     * channelInactive
     * channel 通道 Inactive 不活跃的
     * 当客户端主动断开服务端的链接后，这个通道就是不活跃的。也就是说客户端与服务端的关闭了通信通道并且不可以传输数据
     */
    public void channelInactive(ChannelHandlerContext ctx){
        System.out.println(ctx.channel().localAddress().toString() + " 通道" + channelId + "不活跃！");
        // 关闭流
    }

    /**
     * 功能：读取客户端发送过来的信息
     * 调用时刻：This method is called with the received message, whenever new data is received from a sclient.
     *  注意：参考光放文档实现，关闭资源
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg){
        // 第一种：接收字符串时的处理
        /*try{
//        ByteBuf buf = ((ByteBuf) msg);  // TODO  当发送String 时，此处会转换异常，但官方文档示例如此，;sserver 和 sclient 的handler中都存在，避免他
//        String rev = getMessage(((ByteBuf) msg));
            System.out.println("服务器收到客户端数据:" + msg); // 输出控制台时，需要手动释放信息即finally中代码
        }finally {
            ReferenceCountUtil.release(msg);
        }*/

        // 第一种：接收字符串时的处理,此时netty将自动释放信息
        System.out.println("服务器收到客户端" + channelId + "数据:" + msg);
        readerIdleTime = 1;
//        ctx.write(msg);
//        ctx.flush();  // 也可以直接使用writeAndFlush()方法
    }

    /**
     * 功能：读取完毕客户端发送过来的数据之后的操作
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx){
        System.out.println("服务端接收数据完毕..");
        try {
            Thread.sleep(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 第一种方法：写一个空的buf，并刷新写出区域。完成后关闭sock channel连接。
//        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);  // 注释掉 ，写一个非空的buf
        // .addListener(ChannelFutureListener.CLOSE) 接收信息后是否关闭连接
//        ctx.writeAndFlush(Unpooled.copiedBuffer("你好，客户端，已收到你发送的信息", CharsetUtil.UTF_8)).addListener(ChannelFutureListener.CLOSE);
        ctx.writeAndFlush(Unpooled.copiedBuffer("你好" + channelId + "已收到你发送的信息", CharsetUtil.UTF_8));
        // ctx.flush();
        // 第二种方法：在client端关闭channel连接，这样的话，会触发两次channelReadComplete方法。
        // ctx.flush().close().sync(); // 第三种：改成这种写法也可以，但是这中写法，没有第一种方法的好。
    }

    /**
     * 功能：服务端发生异常的操作，连接关闭前应当返回一个错误码；
     * The exceptionCaught() event handler method is called with a Throwable when an exception was raised by Netty
     * due to an I/O error or by a handler implementation due to the exception thrown while processing events.
     * In most cases, the caught exception should be logged and its associated channel should be closed here,
     * although the implementation of this method can be different depending on what you want to do to
     * deal with an exceptional situation. For example, you might want to send a response message
     * with an error code before closing the connection
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace(); // 打印详细异常堆栈信息
        ctx.close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
//        ChannelId id = ctx.channel().id();
        if (evt instanceof IdleStateEvent){
            IdleStateEvent e = (IdleStateEvent) evt;
            switch (e.state()) {
                case READER_IDLE:
                    System.out.println("通道" + channelId + "读空闲第" + readerIdleTime ++ + "心跳");
                    break;
                case WRITER_IDLE:
                    System.out.println("通道" + channelId + "写空闲第" + writerIdleTime ++ + "心跳");
                    break;
                case ALL_IDLE:
                    System.out.println("通道" + channelId + "读写空闲第" + allIdleTime ++ + "心跳");
                    break;
                default:
                    throw new IOException("未知的IdleStateEvent状态");
            }
        }else{
            super.userEventTriggered(ctx,evt);
        }
        if (readerIdleTime > 3){
            System.out.println(" [server]读空闲超过3次，关闭连接：" + channelId);
            ctx.close();
        }
        if (writerIdleTime > 3){
            System.out.println(" [server]写空闲超过3次，关闭连接：" + channelId);
            ctx.close();
        }
        if (allIdleTime > 3){
            System.out.println(" [server]读写空闲超过3次，关闭连接：" + channelId);
            ctx.close();
        }
    }

}
