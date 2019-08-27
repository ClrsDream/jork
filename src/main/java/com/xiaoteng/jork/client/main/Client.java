package com.xiaoteng.jork.client.main;

import com.alibaba.fastjson.JSON;
import com.xiaoteng.jork.client.config.Config;
import com.xiaoteng.jork.utils.Helper;

import java.io.IOException;

public class Client {

    public void run(String configFile) throws IOException {
        // 读取配置
        String configStr = Helper.fileGetContent(configFile);
        Config config = JSON.parseObject(configStr, Config.class);
    }

}
