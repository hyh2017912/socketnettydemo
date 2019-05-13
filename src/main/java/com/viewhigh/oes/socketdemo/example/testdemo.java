package com.viewhigh.oes.socketdemo.example;

import com.viewhigh.oes.socketdemo.SocketClient;
import com.viewhigh.oes.socketdemo.SocketServer;

import java.net.UnknownHostException;
import java.util.Scanner;

public class testdemo {
    private static Scanner inputScanner = new Scanner(System.in);

    public static void main(String[] args) throws Exception{
        // Start as a sserver or a sclient.
        System.out.println("Please input '0' or '1' to start a sserver or a sclient.");
        System.out.println("Before starting a sclient, make sure a sserver is already running at the same PC.");
        System.out.println("  0: sserver");
        System.out.println("  1: sclient");
        System.out.println("  others: close the program.");
        String input = inputScanner.next();
        switch (input) {
            case "0":
                startServer();
                break;
            case "1":
                startClient();
                break;
            default:
                break;
        }
        inputScanner.close();
    }

    private static void startServer() throws InterruptedException {
        String msg;
        System.out.println("Start a sserver.");
        SocketServer server = new SocketServer();
        server.initSocketServer();
//        SocketServer sserver = new SocketServer(5556, new EchoHandler());
//
//        System.out.println("When type stop and press enter to close the sserver...");
//        while (true){
//            msg = inputScanner.nextLine();
//            if ("stop".equalsIgnoreCase(msg)){
//                System.out.println("stop sserver...");
//                sserver.close();
//                break;
//            }
//        }
    }

    private static void startClient() throws UnknownHostException, InterruptedException {
        String msg;
        System.out.println("Start a sclient.");
        SocketClient client = new SocketClient();
        client.initSocketClient();
//        while(true){
//            System.out.println("when type stop and enter to close this sclient or type something to send to the sserver...");
//            msg = inputScanner.nextLine();
//            if ("stop".equalsIgnoreCase(msg)){
//                System.out.println("stop sclient...");
//                sclient.close();
//                break;
//            }
//            sclient.println(msg);
//            System.out.println("客户端接收信息：" + sclient.readLine());
//        }
}}
