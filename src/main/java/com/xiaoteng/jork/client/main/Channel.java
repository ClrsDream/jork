package com.xiaoteng.jork.client.main;

import com.alibaba.fastjson.JSON;
import com.xiaoteng.jork.client.config.Config;
import com.xiaoteng.jork.constants.Constants;
import com.xiaoteng.jork.messages.ActionMessage;
import com.xiaoteng.jork.messages.RegisterChannelMessage;
import com.xiaoteng.jork.utils.Helper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;

public class Channel {

    private final static Logger log = LogManager.getLogger(Channel.class);

    private int id;

    private Config config;

    public Channel(int id, Config config) {
        this.id = id;
        this.config = config;
    }

    public void run() {
        try {
            Socket transportClient = new Socket(config.getServer_host(), config.getServer_transport_port());
            Socket localClient = new Socket(config.getAddress(), config.getPort());

            // 发起transportClient
            Thread t1 = new Thread(() -> {
                try {
                    PrintWriter printWriter = new PrintWriter(transportClient.getOutputStream());
                    RegisterChannelMessage registerChannelMessage = new RegisterChannelMessage(id);
                    ActionMessage actionMessage = new ActionMessage(Constants.RESPONSE_METHOD_NEW_CHANNEL, JSON.toJSONString(registerChannelMessage));
                    String s = JSON.toJSONString(actionMessage);
                    log.info("发起新的transport连接 {}", s);
                    printWriter.println(s);
                    printWriter.flush();

                    // 监听
                    Helper.ioCopy(transportClient, localClient);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            t1.start();

            Thread t2 = new Thread(() -> {
                try {
                    Helper.ioCopy(localClient, transportClient);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            t2.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
