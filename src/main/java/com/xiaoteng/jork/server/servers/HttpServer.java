package com.xiaoteng.jork.server.servers;

import com.alibaba.fastjson.JSON;
import com.xiaoteng.jork.server.channel.Channel;
import com.xiaoteng.jork.server.channel.ChannelTable;
import com.xiaoteng.jork.server.main.Client;
import com.xiaoteng.jork.server.main.RegisterTable;
import com.xiaoteng.jork.server.messages.response.NewChannelMessage;
import com.xiaoteng.jork.utils.Helper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author xiaoteng
 */
public class HttpServer {

    private final static Logger log = LogManager.getLogger(HttpServer.class);

    private final static int PORT = 80;

    private ExecutorService executorService = Executors.newFixedThreadPool(10);

    public void listener() {
        try {
            ServerSocket ss = new ServerSocket(PORT);
            while (true) {
                Socket socket = ss.accept();
                executorService.submit(() -> {
                    // 客户来了一个新的连接请求，我要判断这个请求属于哪个client
                    // 然后让该client重新发起一个connection与此连接建立隧道
                    // 专门处理本次请求

                    String domain = Helper.getChildDomain(socket.getInetAddress().getHostName());

                    ArrayList<Client> clients = RegisterTable.getClients(PORT);
                    for (Client client : clients) {
                        if (!client.getDomain().equals(domain)) {
                            continue;
                        }
                        // 找到当前请求对应client，这里需要发送一个消息给client
                        // 让它重新发起一个connection用来处理本次请求
                        log.info("将channel注册到channelTable");
                        int id = client.hashCode();
                        Channel channel = new Channel(socket, null);
                        ChannelTable.add(id, channel);

                        // 写入消息
                        NewChannelMessage ncm = new NewChannelMessage(id);
                        try {
                            PrintWriter printWriter = new PrintWriter(client.getSocket().getOutputStream());
                            printWriter.println(JSON.toJSONString(ncm));
                            printWriter.flush();
                        } catch (IOException e) {
                            log.info("无法获取client的socket的outputStream");
                            e.printStackTrace();
                        }
                    }
                });
            }
        } catch (IOException e) {
            log.info("无法监听80端口，可能是被占用了");
            e.printStackTrace();
        }
    }

}
