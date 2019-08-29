package com.xiaoteng.jork.server.servers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author xiaoteng
 */
public class HttpServer {

    private final static Logger log = LogManager.getLogger(HttpServer.class);

    private final static int PORT = 80;

    private final static int THREAD_NUM = 10;

    public void listener() {
        // 线程池
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_NUM);

        try {
            ServerSocket ss = new ServerSocket(PORT);
            log.info("监听{}端口", PORT);
            while (true) {
                Socket socket = ss.accept();
                log.info("收到新的连接");
                executorService.submit(() -> {

                });
            }
        } catch (IOException e) {
            log.info("无法监听80端口，可能是被占用了");
            e.printStackTrace();
        }
    }

}
