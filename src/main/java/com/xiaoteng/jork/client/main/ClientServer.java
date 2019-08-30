package com.xiaoteng.jork.client.main;

import com.alibaba.fastjson.JSON;
import com.xiaoteng.jork.client.config.Config;
import com.xiaoteng.jork.constants.Constants;
import com.xiaoteng.jork.messages.ActionMessage;
import com.xiaoteng.jork.messages.AuthMessage;
import com.xiaoteng.jork.messages.ClientRegisterMessage;
import com.xiaoteng.jork.messages.RegisterChannelMessage;
import com.xiaoteng.jork.utils.Helper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientServer {

    private final static Logger log = LogManager.getLogger(ClientServer.class);

    private Config config;

    public ClientServer(Config config) {
        this.config = config;
    }

    public void run() {
        Socket c = null;
        try {
            c = new Socket(this.config.getServer_host(), this.config.getServer_port());
            System.out.printf("已成功连接到jork服务端%s:%s\n", config.getServer_host(), config.getServer_port());

            // 发送认证消息
            AuthMessage authMessage = new AuthMessage(config.getUsername(), config.getPassword());
            log.info("发送jorkClient认证消息 {}", authMessage);
            Helper.sendMessage(Constants.METHOD_AUTH, authMessage, c);

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(c.getInputStream()));
            String s;
            while ((s = bufferedReader.readLine()) != null) {
                log.info("收到消息{}", s);
                // 空检测
                if (s.isEmpty()) {
                    continue;
                }
                if (!JSON.isValid(s)) {
                    log.info("数据{}不是json格式，跳过处理", s);
                    continue;
                }

                ActionMessage ms = JSON.parseObject(s, ActionMessage.class);
                switch (ms.getMethod()) {
                    case Constants.METHOD_AUTH_RESULT:
                        // 发送客户端的注册信息
                        ClientRegisterMessage clientRegisterMessage = new ClientRegisterMessage(config.getDomain(), config.getListen_port(), config.getProtocol());
                        log.info("发送jorkClient注册信息 {}", clientRegisterMessage);
                        Helper.sendMessage(Constants.METHOD_REGISTER, clientRegisterMessage, c);
                        break;
                    case Constants.RESPONSE_METHOD_NEW_CHANNEL:
                        // 收到发起新connection的请求
                        RegisterChannelMessage rcm = JSON.parseObject(ms.getContent(), RegisterChannelMessage.class);
                        Channel channel = new Channel(rcm.getId(), config);
                        channel.run();
                        break;
                    default:
                        log.warn("method[{}]不支持", ms.getMethod());
                }
            }
            System.out.println("服务端已断开");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
