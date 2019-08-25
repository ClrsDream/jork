package com.xiaoteng.jork.server.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author xiaoteng
 */
public class Server {

    private final static Logger log = LogManager.getLogger(Server.class);
    /**
     * 监听的服务端口
     */
    private final static int CLIENT_SERVICE_PORT = 5590;
    /**
     * 线程数量
     */
    private final static int THREAD_NUM = 10;

    public void run() throws IOException {
        // 初始化线程池
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_NUM);

        ServerSocket ss = new ServerSocket(CLIENT_SERVICE_PORT);
        log.info("程序开始运行，监听{}端口中...", CLIENT_SERVICE_PORT);
        while (true) {
            Socket client = ss.accept();
            log.info("收到来自客户端的请求");
            // 提交到线程池里面处理，预防阻塞
            executorService.submit(new Connection(client));
        }
    }

}
