package com.xiaoteng.jork.server.main;

import com.xiaoteng.jork.server.servers.HttpServer;
import com.xiaoteng.jork.server.servers.TcpServer;
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
    private final static int THREAD_NUM = 30;

    public static volatile ExecutorService executorService;

    public void run() {
        // 初始化线程池
        executorService = Executors.newFixedThreadPool(THREAD_NUM);

        // 监听本地80服务
        executorService.submit(new HttpServer());
        // 监听本地tcp服务
        executorService.submit(new TcpServer());

        // transport服务
        executorService.submit(new TransportServer());

        try {
            ServerSocket ss = new ServerSocket(CLIENT_SERVICE_PORT);
            log.info("监听{}端口...", CLIENT_SERVICE_PORT);

            while (true) {
                Socket client = ss.accept();
                log.info("新的jork客户端的请求");
                // 提交到线程池里面处理，预防阻塞
                executorService.submit(new NewJorkConnection(client));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
