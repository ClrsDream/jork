package com.xiaoteng.jork.server.main;

import com.alibaba.fastjson.JSON;
import com.xiaoteng.jork.constants.Constants;
import com.xiaoteng.jork.messages.ActionMessage;
import com.xiaoteng.jork.messages.RegisterChannelMessage;
import com.xiaoteng.jork.server.storage.JorkTransportClientsStorage;
import com.xiaoteng.jork.server.storage.LocalClientsStorage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TransportServer implements Runnable {

    private final static Logger log = LogManager.getLogger(TransportServer.class);

    private final static int PORT = 5591;

    @Override
    public void run() {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        try {
            ServerSocket ss = new ServerSocket(PORT);
            log.info("开始监听{}", PORT);
            while (true) {
                Socket socket = ss.accept();
                executorService.submit(() -> {
                    try {
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        String s = bufferedReader.readLine();
                        if (!JSON.isValid(s)) {
                            log.info("无效的Json字符{}", s);
                            return;
                        }
                        ActionMessage actionMessage = JSON.parseObject(s, ActionMessage.class);
                        if (!Constants.RESPONSE_METHOD_NEW_CHANNEL.equals(actionMessage.getMethod())) {
                            log.info("当前方法不支持{}", actionMessage);
                            return;
                        }

                        // 注册Socket
                        RegisterChannelMessage registerChannelMessage = JSON.parseObject(actionMessage.getContent(), RegisterChannelMessage.class);
                        JorkTransportClientsStorage.add(registerChannelMessage.getId(), socket);

                        // 开始监听
                        int c;
                        Socket localClient = LocalClientsStorage.get(registerChannelMessage.getId());
                        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(localClient.getOutputStream()));
                        while ((c = bufferedReader.read()) != -1) {
                            bufferedWriter.write(c);
                            bufferedWriter.flush();
                        }
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
