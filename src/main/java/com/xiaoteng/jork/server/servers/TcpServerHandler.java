package com.xiaoteng.jork.server.servers;

import com.alibaba.fastjson.JSON;
import com.xiaoteng.jork.constants.Constants;
import com.xiaoteng.jork.messages.ActionMessage;
import com.xiaoteng.jork.messages.RegisterChannelMessage;
import com.xiaoteng.jork.server.main.JorkClient;
import com.xiaoteng.jork.server.storage.JorkClientsStorage;
import com.xiaoteng.jork.server.storage.JorkTransportClientsStorage;
import com.xiaoteng.jork.server.storage.LocalClientsStorage;
import com.xiaoteng.jork.utils.Helper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class TcpServerHandler implements Runnable {

    private final static Logger log = LogManager.getLogger(HttpServer.class);

    private Socket socket;

    public TcpServerHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            // 获取域名
            log.info("客户端信息 {}", socket.getInetAddress());
            String domain = socket.getInetAddress().getHostName();
            // 查找该域名是否有jorkClient注册
            JorkClient jorkClient = JorkClientsStorage.findByDomain(domain);
            if (jorkClient == null) {
                // 没有jorkClient注册，直接关闭连接
                log.info("当前域名{}未注册", domain);
                socket.close();
                return;
            }
            // 将当前连接存储在本地
            LocalClientsStorage.add(socket.hashCode(), socket);

            // 判断当前的连接是否关联了jorkTransportClient
            Socket jorkTransportClient = JorkTransportClientsStorage.get(socket.hashCode());
            if (jorkTransportClient == null) {
                // 没有关联jorkTransportClient
                // 现在需要发一个消息给jorkClient且覆盖上socket.hashCode()
                // 然后jorkClient接受到消息之后会重新发起一个连接，且带上socket.hashCode()
                // 最后完整一次关联此次conn的注册
                log.info("发送消息给jorkClient，让它重新发起一个新的连接，专门用于传输");
                RegisterChannelMessage registerChannelMessage = new RegisterChannelMessage(socket.hashCode());
                ActionMessage actionMessage = new ActionMessage(Constants.RESPONSE_METHOD_NEW_CHANNEL, JSON.toJSONString(registerChannelMessage));
                PrintWriter printWriter = new PrintWriter(jorkClient.getSocket().getOutputStream());
                printWriter.println(JSON.toJSONString(actionMessage));
                printWriter.flush();

                // 这里等待10的时间，如果jorkClient没有响应，那么直接终止这个连接
                int times = 0;
                while (times < 20) {
                    times++;
                    log.info("开始等待jorkClient发起transport连接，次数{}次", times);
                    jorkTransportClient = JorkTransportClientsStorage.get(socket.hashCode());
                    if (jorkTransportClient != null) {
                        // 已经注册
                        break;
                    }
                    Thread.sleep(500);
                }
                if (jorkTransportClient == null) {
                    log.info("jorkClient一直未发起transport的连接");
                    socket.close();
                    return;
                }
            }

            // 到这里，本次的conn与jorkClient的conn已成功关联
            // 接下来需要监听双方的写入事件，并做同步写入操作
            log.info("关联通道已建立成功，开启同步写入...");
            Helper.ioCopy(socket, jorkTransportClient);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
