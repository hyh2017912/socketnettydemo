package com.viewhigh.oes.socketdemo.utils;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.CharsetUtil;

import java.util.Scanner;

/**
 * 消息发送工具
 */
public class SockerUtils {
    private static Scanner inputScanner = new Scanner(System.in);
    public static void sendMsg(ChannelHandlerContext ctx, String s){
/*        System.out.println("请输入你要发送的信息，并按回车键确认发送");
        String sendInfo = inputScanner.nextLine();
        if ("exit".equalsIgnoreCase(sendInfo)){
            ctx.close();
            return;
        }*/
        try {
            Thread.sleep(6000);
            System.out.println("开始休眠。。。");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String sendInfo = "数据内容：" + s + ",时间：" + System.currentTimeMillis();
        System.out.println( s + "准备发送的数据包：" + sendInfo);
//        ctx.writeAndFlush(Unpooled.copiedBuffer(sendInfo, CharsetUtil.UTF_8)); // 必须有flush
        ctx.writeAndFlush(Unpooled.copiedBuffer(sendInfo.getBytes())); // 必须有flush
    }

    public static void closed(ChannelHandlerContext ctx){
        ctx.close();
    }
}
