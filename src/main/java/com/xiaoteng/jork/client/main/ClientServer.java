package com.xiaoteng.jork.client.main;

import com.alibaba.fastjson.JSON;
import com.xiaoteng.jork.client.config.Config;
import com.xiaoteng.jork.constants.Constants;
import com.xiaoteng.jork.messages.ActionMessage;
import com.xiaoteng.jork.messages.RegisterChannelMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
            System.out.printf("已成功连接到服务端%s:%s\n", config.getServer_host(), config.getServer_port());
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
