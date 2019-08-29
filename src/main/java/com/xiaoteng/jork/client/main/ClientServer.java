package com.xiaoteng.jork.client.main;

import com.alibaba.fastjson.JSON;
import com.xiaoteng.jork.client.config.Config;
import com.xiaoteng.jork.constants.Constants;
import com.xiaoteng.jork.messages.ActionMessage;
import com.xiaoteng.jork.messages.AuthMessage;
import com.xiaoteng.jork.messages.ClientRegisterMessage;
import com.xiaoteng.jork.messages.RegisterChannelMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientServer {

    private final static Logger log = LogManager.getLogger(ClientServer.class);
    private final static int THREAD_NUM = 10;
    private Config config;

    public ClientServer(Config config) {
        this.config = config;
    }

    public void run() {
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_NUM);

        Socket c = null;
        try {
            c = new Socket(this.config.getServer_host(), this.config.getServer_port());
            System.out.printf("已成功连接到jork服务端%s:%s\n", config.getServer_host(), config.getServer_port());
            PrintWriter printWriter = new PrintWriter(c.getOutputStream());

            // 发送认证消息
            AuthMessage authMessage = new AuthMessage(config.getUsername(), config.getPassword());
            ActionMessage actionMessage = new ActionMessage(Constants.METHOD_AUTH, JSON.toJSONString(authMessage));
            String actionMessageStr = JSON.toJSONString(actionMessage);
            log.info("发送认证信息到jork服务端,内容：{}", actionMessageStr);
            printWriter.println(actionMessageStr);
            printWriter.flush();

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
                        // 发送客户端注册信息
                        log.info("正在将jork客户端信息注册到jork服务端...");
                        ClientRegisterMessage clientRegisterMessage = new ClientRegisterMessage(config.getDomain(), config.getListen_port(), config.getProtocol());
                        ActionMessage actionMessage1 = new ActionMessage(Constants.METHOD_REGISTER, JSON.toJSONString(clientRegisterMessage));
                        printWriter.println(JSON.toJSONString(actionMessage1));
                        printWriter.flush();
                        break;
                    case Constants.RESPONSE_METHOD_NEW_CHANNEL:
                        // 收到发起新connection的请求
                        RegisterChannelMessage rcm = JSON.parseObject(ms.getContent(), RegisterChannelMessage.class);
                        IoCopy ioCopy = new IoCopy(config, rcm.getId());
                        // todo 防止重复注册，做一个id限制
                        executorService.submit(ioCopy::localSocketHandler);
                        executorService.submit(ioCopy::serverSocketHandler);
                        break;
                    default:
                        log.warn("method{}不支持", ms.getMethod());
                }
            }
            System.out.println("服务端已断开");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
