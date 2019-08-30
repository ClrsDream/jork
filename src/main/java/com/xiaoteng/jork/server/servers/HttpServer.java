package com.xiaoteng.jork.server.servers;

import com.xiaoteng.jork.server.main.Server;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * HTTP监听服务
 *
 * @author xiaoteng
 */
public class HttpServer {

    private final static Logger log = LogManager.getLogger(HttpServer.class);

    private final static int PORT = 80;

    public void listener() {
        ServerSocket ss;
        try {
            ss = new ServerSocket(PORT);
            log.info("监听{}端口", PORT);
            while (true) {
                Socket socket = ss.accept();
                log.info("收到新的HTTP连接");
                Server.executorService.submit(new HttpServerHandler(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
