package com.xiaoteng.jork.server.servers;

import com.xiaoteng.jork.server.main.Server;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServer implements Runnable {

    private final static Logger log = LogManager.getLogger(TcpServer.class);

    private final static int PORT = 5592;

    @Override
    public void run() {
        ServerSocket ss = null;
        try {
            ss = new ServerSocket(PORT);
            log.info("监听{}端口...", PORT);
            while (true) {
                Socket socket = ss.accept();
                log.info("收到新的tcp连接");
                Server.executorService.submit(new TcpServerHandler(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
