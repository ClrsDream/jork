package com.xiaoteng.jork.server.servers;

import com.alibaba.fastjson.JSON;
import com.xiaoteng.jork.constants.Constants;
import com.xiaoteng.jork.server.channel.Channel;
import com.xiaoteng.jork.server.channel.ChannelTable;
import com.xiaoteng.jork.server.main.Client;
import com.xiaoteng.jork.server.main.RegisterTable;
import com.xiaoteng.jork.server.messages.response.NewChannelMessage;
import com.xiaoteng.jork.server.messages.response.ResponseMessage;
import com.xiaoteng.jork.utils.Helper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

    private final static int THREAD_NUM = 10;

    public void listener() {
        // 线程池
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_NUM);

        try {
            ServerSocket ss = new ServerSocket(PORT);
            while (true) {
                Socket socket = ss.accept();
                executorService.submit(() -> {
                    // 客户来了一个新的连接请求
                    // 先判断这个请求是否已经注册，注册表是ChannelTable
                    // 没有注册的先注册，注册完成之后
                    // 然后让该client重新发起一个connection与此连接建立隧道
                    // 专门处理本次请求

                    Integer userClientId = socket.hashCode();
                    Channel channel = ChannelTable.getChannel(userClientId);
                    if (channel == null) {
                        // 获取当前用户请求的域名[二级域名]
                        String domain = Helper.getChildDomain(socket.getInetAddress().getHostName());
                        log.info("完成域名{}，子域名{}", socket.getInetAddress().getHostName(), domain);

                        ArrayList<Client> clients = RegisterTable.getClients(PORT);
                        for (Client client : clients) {
                            if (!client.getDomain().equals(domain)) {
                                continue;
                            }
                            // 找到当前请求对应client，这里需要发送一个消息给client
                            // 让它重新发起一个connection用来处理本次请求
                            log.info("将channel注册到channelTable");
                            int id = client.hashCode();
                            Channel c = new Channel(socket, null);
                            ChannelTable.add(id, c);

                            // 写入消息
                            NewChannelMessage ncm = new NewChannelMessage(id);
                            ResponseMessage rm = new ResponseMessage(Constants.RESPONSE_METHOD_NEW_CHANNEL, JSON.toJSONString(ncm));
                            try {
                                PrintWriter printWriter = new PrintWriter(client.getSocket().getOutputStream());
                                String m = JSON.toJSONString(rm);
                                log.info("向客户端写入数据{}", m);
                                printWriter.println(m);
                                printWriter.flush();
                            } catch (IOException e) {
                                log.info("无法获取client的socket的outputStream");
                                e.printStackTrace();
                            }
                        }
                    }

                    try {

                        // 等待客户端发起连接并注册
                        int times = 0;
                        boolean isContinue = true;
                        while (true) {
                            times++;
                            channel = ChannelTable.getChannel(userClientId);
                            if (channel.getClientSocket() != null) {
                                // 说明客户端已经发起了新的连接且已经完成了注册
                                break;
                            }
                            if (times > 30) {
                                // 15s超时，直接退出
                                isContinue = false;
                                socket.close();
                                break;
                            }
                            Thread.sleep(500);
                        }

                        if (isContinue) {
                            // 兼容来自用户的请求数据并写入到先关联的客户端
                            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                            String line;
                            PrintWriter printWriter = new PrintWriter(channel.getClientSocket().getOutputStream());
                            if ((line = bufferedReader.readLine()) != null) {
                                printWriter.println(line);
                            }
                        }

                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (IOException e) {
            log.info("无法监听80端口，可能是被占用了");
            e.printStackTrace();
        }
    }

}
