package com.xiaoteng.jork.client.main;

import com.alibaba.fastjson.JSON;
import com.xiaoteng.jork.client.config.Config;
import com.xiaoteng.jork.constants.Constants;
import com.xiaoteng.jork.messages.ActionMessage;
import com.xiaoteng.jork.messages.RegisterChannelMessage;

import java.io.*;
import java.net.Socket;

public class IoCopy {

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
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(localSocket.getOutputStream()));
            int c;
            while ((c = bufferedReader.read()) != -1) {
                bufferedWriter.write(c);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void serverSocketHandler() {
        try {
            serverSocket = new Socket(config.getServer_host(), config.getServer_port());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(serverSocket.getOutputStream()));
            PrintWriter printWriter = new PrintWriter(serverSocket.getOutputStream());
            // 先注册
            RegisterChannelMessage rcm = new RegisterChannelMessage(registerId);
            ActionMessage am = new ActionMessage(Constants.RESPONSE_METHOD_NEW_CHANNEL, JSON.toJSONString(rcm));
            printWriter.println(JSON.toJSONString(am));

            // 然后监听写入
            int c;
            while ((c = bufferedReader.read()) != -1) {
                bufferedWriter.write(c);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
