package com.xiaoteng.jork.server.main;

import com.alibaba.fastjson.JSON;
import com.xiaoteng.jork.constants.Constants;
import com.xiaoteng.jork.messages.ActionMessage;
import com.xiaoteng.jork.messages.RegisterChannelMessage;
import com.xiaoteng.jork.server.storage.JorkTransportClientsStorage;
import com.xiaoteng.jork.server.storage.LocalClientsStorage;
import com.xiaoteng.jork.utils.Helper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class TransportServer implements Runnable {

    private final static Logger log = LogManager.getLogger(TransportServer.class);

    private final static int PORT = 5591;

    @Override
    public void run() {
        try {
            ServerSocket ss = new ServerSocket(PORT);
            log.info("监听{}端口...", PORT);
            while (true) {
                Socket socket = ss.accept();
                Server.executorService.submit(() -> {
                    try {
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        String s = bufferedReader.readLine();
                        if (!JSON.isValid(s)) {
                            log.info("无效的Json字符 {}", s);
                            return;
                        }
                        ActionMessage actionMessage = JSON.parseObject(s, ActionMessage.class);
                        if (!Constants.RESPONSE_METHOD_NEW_CHANNEL.equals(actionMessage.getMethod())) {
                            log.info("当前方法不支持 {}", actionMessage);
                            return;
                        }

                        // 注册Socket
                        RegisterChannelMessage registerChannelMessage = JSON.parseObject(actionMessage.getContent(), RegisterChannelMessage.class);
                        JorkTransportClientsStorage.add(registerChannelMessage.getId(), socket);

                        // 开始监听
                        Socket localClient = LocalClientsStorage.get(registerChannelMessage.getId());
                        Helper.ioCopy(socket, localClient);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
