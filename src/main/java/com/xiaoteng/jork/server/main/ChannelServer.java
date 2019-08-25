package com.xiaoteng.jork.server.main;

import com.alibaba.fastjson.JSON;
import com.xiaoteng.jork.server.channel.Channel;
import com.xiaoteng.jork.server.channel.ChannelTable;
import com.xiaoteng.jork.server.messages.request.ActionMessage;
import com.xiaoteng.jork.server.messages.request.NewChannelMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author xiaoteng
 */
public class ChannelServer {

    private final static Logger log = LogManager.getLogger(Server.class);

    /**
     * 监听的服务端口
     */
    private final static int CLIENT_SERVICE_PORT = 5591;

    /**
     * 线程数量
     */
    private final static int THREAD_NUM = 10;

    public void run() throws IOException {
        // 线程池
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_NUM);
        // 监听服务
        ServerSocket ss = new ServerSocket(CLIENT_SERVICE_PORT);
        log.info("channel服务已运行，监听端口{}", CLIENT_SERVICE_PORT);

        // 常驻处理
        while (true) {
            Socket client = ss.accept();
            log.info("收到来自客户端的请求");
            // 提交到线程池里面处理，预防阻塞
            executorService.submit(() -> {
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        // 判断当前的client是否已经注册
                        Integer clientId = client.hashCode();
                        Integer userClientId = ChannelTable.getUserClientId(clientId);
                        if (userClientId == 0) {
                            // 未注册，那么就将当前的client注册
                            ActionMessage actionMessage = JSON.parseObject(line, ActionMessage.class);
                            if ("new_channel".equals(actionMessage.getActionMethod())) {
                                NewChannelMessage newChannelMessage = JSON.parseObject(actionMessage.getContent(), NewChannelMessage.class);
                                ChannelTable.registerClient(clientId, newChannelMessage.getId());
                            }
                        } else {
                            // 已注册，直接写入
                            Channel channel = ChannelTable.getChannel(userClientId);
                            Socket userSocket = channel.getUserSocket();
                            if (userSocket != null) {
                                PrintWriter printWriter = new PrintWriter(userSocket.getOutputStream());
                                printWriter.println(line);
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

}
