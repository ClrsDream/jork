package com.xiaoteng.jork.client.main;

import com.alibaba.fastjson.JSON;
import com.xiaoteng.jork.client.config.Config;
import com.xiaoteng.jork.utils.Helper;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {

    public static ExecutorService executorService;

    public void run(String configFile) throws IOException {
        executorService = Executors.newFixedThreadPool(30);

        // 读取配置
        String configStr = Helper.fileGetContent(configFile);
        Config config = JSON.parseObject(configStr, Config.class);
        // 运行
        ClientServer clientServer = new ClientServer(config);
        clientServer.run();
    }

}
