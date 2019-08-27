package com.xiaoteng.jork.client.main;

import com.alibaba.fastjson.JSON;
import com.xiaoteng.jork.client.config.Config;
import com.xiaoteng.jork.client.messages.reesponse.ChannelMessage;
import com.xiaoteng.jork.client.messages.reesponse.ResponseMessage;
import com.xiaoteng.jork.client.messages.request.NewChannelMessage;
import com.xiaoteng.jork.constants.Constants;
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
            System.out.printf("已成功连接到服务端%s:%s\n", config.getServer_host(), config.getServer_port());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(c.getInputStream()));
            String s;
            while ((s = bufferedReader.readLine()) != null) {
                // 空检测
                if (s.isEmpty()) {
                    continue;
                }
                if (!JSON.isValid(s)) {
                    log.info("数据{}不是json格式，跳过处理", s);
                    continue;
                }

                ResponseMessage ms = JSON.parseObject(s, ResponseMessage.class);
                log.info("收到消息{}", ms.getContent());
                switch (ms.getMethod()) {
                    case Constants
                            .RESPONSE_METHOD_NEW_CHANNEL:
                        // 收到发起新connection的请求
                        NewChannelMessage ncm = JSON.parseObject(ms.getContent(), NewChannelMessage.class);
                        executorService.submit(() -> {
                            try {
                                // 发起新的连接
                                Socket conn = new Socket(config.getServer_host(), config.getServer_port());
                                // 注册
                                PrintWriter printWriter = new PrintWriter(conn.getOutputStream());
                                ChannelMessage channelMessage = new ChannelMessage(ncm.getId());
                                ResponseMessage responseMessage = new ResponseMessage(Constants.RESPONSE_METHOD_NEW_CHANNEL, JSON.toJSONString(channelMessage));
                                printWriter.println(JSON.toJSONString(responseMessage));

                                // 开始监听本地的端口服务
                                Socket connLocal = new Socket(config.getAddress(), config.getPort());
                                BufferedReader bufferedReader1 = new BufferedReader(new InputStreamReader(connLocal.getInputStream()));
                                String s1;
                                while ((s1 = bufferedReader1.readLine()) != null) {
                                    // 将接收到的消息转写到服务端
                                    printWriter.println(s1);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
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
