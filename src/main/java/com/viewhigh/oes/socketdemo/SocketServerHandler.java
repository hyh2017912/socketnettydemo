package com.viewhigh.oes.socketdemo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

public class SocketServerHandler extends SimpleChannelInboundHandler {
     // SimpleChannelInboundHandler 继承ChannelHandlerAdapter

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Object msg){
        //打印出客户端地址
//        System.out.println(ctx.channel().remoteAddress()+", "+msg);
//        ctx.channel().writeAndFlush("form server: "+ UUID.randomUUID());
    }

    /*
     * channelAction
     *
     * channel 通道 action 活跃的
     *
     * 当客户端主动链接服务端的链接后，这个通道就是活跃的了。也就是客户端与服务端建立了通信通道并且可以传输数据
     *
     */
    public void channelActive(ChannelHandlerContext ctx){
        System.out.println(ctx.channel().localAddress().toString() + " 通道已激活！");
    }

    /*
     * channelInactive
     *
     * channel 通道 Inactive 不活跃的
     *
     * 当客户端主动断开服务端的链接后，这个通道就是不活跃的。也就是说客户端与服务端的关闭了通信通道并且不可以传输数据
     *
     */
    public void channelInactive(ChannelHandlerContext ctx){
        System.out.println(ctx.channel().localAddress().toString() + " 通道不活跃！");
        // 关闭流

    }

    /**
     *
     * @author Taowd
     * TODO  此处用来处理收到的数据中含有中文的时  出现乱码的问题
     * 2017年8月31日 下午7:57:28
     * @param buf
     * @return
     */
    private String getMessage(ByteBuf buf) {
        byte[] con = new byte[buf.readableBytes()];
        buf.readBytes(con);
        try {
            return new String(con, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 功能：读取客户端发送过来的信息
     * 调用时刻：This method is called with the received message, whenever new data is received from a client.
     *  注意：参考光放文档实现，关闭资源
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg){
        // 第一种：接收字符串时的处理
        /*try{
//        ByteBuf buf = ((ByteBuf) msg);  // TODO  当发送String 时，此处会转换异常，但官方文档示例如此，;server 和 client 的handler中都存在，避免他
//        String rev = getMessage(((ByteBuf) msg));
            System.out.println("服务器收到客户端数据:" + msg); // 输出控制台时，需要手动释放信息即finally中代码
        }finally {
            ReferenceCountUtil.release(msg);
        }*/

        // 第一种：接收字符串时的处理,此时netty将自动释放信息
        System.out.println("服务器收到客户端数据:" + msg);
//        ctx.write(msg);
//        ctx.flush();  // 也可以直接使用writeAndFlush()方法
    }

    /**
     * 功能：读取完毕客户端发送过来的数据之后的操作
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx){
        System.out.println("服务端接收数据完毕..");
        // 第一种方法：写一个空的buf，并刷新写出区域。完成后关闭sock channel连接。
//        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);  // 注释掉 ，写一个非空的buf
        // .addListener(ChannelFutureListener.CLOSE) 接收信息后是否关闭连接
//        ctx.writeAndFlush(Unpooled.copiedBuffer("你好，客户端，已收到你发送的信息", CharsetUtil.UTF_8)).addListener(ChannelFutureListener.CLOSE);
        ctx.writeAndFlush(Unpooled.copiedBuffer("你好，客户端，已收到你发送的信息", CharsetUtil.UTF_8));
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
//        System.out.println("异常信息：\r\n" + cause.getMessage());
    }

}
