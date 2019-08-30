package com.xiaoteng.jork.client.main;

import com.alibaba.fastjson.JSON;
import com.xiaoteng.jork.client.config.Config;
import com.xiaoteng.jork.constants.Constants;
import com.xiaoteng.jork.messages.ActionMessage;
import com.xiaoteng.jork.messages.RegisterChannelMessage;
import com.xiaoteng.jork.utils.Helper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Channel {

    private final static Logger log = LogManager.getLogger(Channel.class);

    private final static int TIMEOUT = 30000;

    private int id;

    private Config config;

    public Channel(int id, Config config) {
        this.id = id;
        this.config = config;
    }

    public void run() {
        try {
            Socket transportClient = new Socket(config.getServer_host(), config.getServer_transport_port());

            // 发起transportClient
            Client.executorService.submit(() -> {
                try {
                    RegisterChannelMessage registerChannelMessage = new RegisterChannelMessage(id);
                    log.info("发起新的transport连接 {}", registerChannelMessage);
                    Helper.sendMessage(Constants.RESPONSE_METHOD_NEW_CHANNEL, registerChannelMessage, transportClient);
                    // 等待服务端的channel注册成功消息
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(transportClient.getInputStream()));
                    log.info("等待channel注册成功回写的信息...");
                    String s = bufferedReader.readLine();
                    log.info("收到channel注册回写信息 {}", s);
                    if (!JSON.isValid(s)) {
                        log.warn("channel注册回调信息不是有效json {}", s);
                        transportClient.close();
                        return;
                    }
                    ActionMessage actionMessage = JSON.parseObject(s, ActionMessage.class);
                    if (!Constants.METHOD_CHANNEL_REGISTERED.equals(actionMessage.getMethod())) {
                        log.warn("channel注册回调的信息不是想要的method {}", s);
                        transportClient.close();
                        return;
                    }

                    Socket localClient = new Socket(config.getAddress(), config.getPort());
                    Client.executorService.submit(() -> {
                        try {
                            Helper.ioCopy(transportClient, localClient);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

                    // 本地服务的io同步开启
                    Client.executorService.submit(() -> {
                        try {
                            Helper.ioCopy(localClient, transportClient);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
