package com.xiaoteng.jork.client.main;

import com.alibaba.fastjson.JSON;
import com.xiaoteng.jork.client.config.Config;
import com.xiaoteng.jork.constants.Constants;
import com.xiaoteng.jork.messages.ActionMessage;
import com.xiaoteng.jork.messages.RegisterChannelMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;

public class IoCopy {

    private final static Logger log = LogManager.getLogger(IoCopy.class);

    private volatile Socket localSocket;

    private volatile Socket serverSocket;

    private int registerId;

    private Config config;

    public IoCopy(Config config, int registerId) {
        this.config = config;
        this.registerId = registerId;
    }

    public int getRegisterId() {
        return registerId;
    }

    public void setRegisterId(int registerId) {
        this.registerId = registerId;
    }

    public void localSocketHandler() {
        try {
            // 该方法会监听本地的服务
            localSocket = new Socket(config.getAddress(), config.getPort());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(localSocket.getInputStream()));
            PrintWriter printWriter = new PrintWriter(serverSocket.getOutputStream());
            String s;
            while ((s = bufferedReader.readLine()) != null) {
                log.info("收到来自localClient的消息 {}", s);
                printWriter.println(s);
                printWriter.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void serverSocketHandler() {
        try {
            serverSocket = new Socket(config.getServer_host(), config.getServer_transport_port());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
            PrintWriter printWriter = new PrintWriter(serverSocket.getOutputStream());
            // 先注册
            RegisterChannelMessage rcm = new RegisterChannelMessage(registerId);
            ActionMessage am = new ActionMessage(Constants.RESPONSE_METHOD_NEW_CHANNEL, JSON.toJSONString(rcm));
            String s = JSON.toJSONString(am);
            log.info("发送transportClient的注册消息{}", s);
            printWriter.println(JSON.toJSONString(am));
            printWriter.flush();

            // 然后监听写入
            PrintWriter printWriter1 = new PrintWriter(new OutputStreamWriter(localSocket.getOutputStream()));
            String s1;
            while ((s1 = bufferedReader.readLine()) != null) {
                log.info("收到来自serverSocket的消息 {}", s1);
                printWriter1.println(s1);
                printWriter1.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
