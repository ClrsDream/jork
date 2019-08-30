package com.xiaoteng.jork;

import com.xiaoteng.jork.client.main.Client;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class App {

    private final static Logger log = LogManager.getLogger(App.class);

    private final static String JORK = "     ____.________ __________ ____  __.\n" +
            "    |    |\\_____  \\\\______   \\    |/ _|\n" +
            "    |    | /   |   \\|       _/      <  \n" +
            "/\\__|    |/    |    \\    |   \\    |  \\ \n" +
            "\\________|\\_______  /____|_  /____|__ \\\n" +
            "                  \\/       \\/        \\/";

    public static void main(String[] args) throws IOException {
        System.out.println(JORK);
        Client client = new Client();
        client.run("/Users/xiaoteng/IdeaProjects/jork/config.json");
//        // 服务端运行模式
//        String model = System.getProperty("jork.run.model");
//        String configPath = System.getProperty("jork.client.config");
//        if (model == null || model.isEmpty()) {
//            System.out.println("请指定运行模式，es: -Djork.run.model=server");
//            System.exit(1);
//        }
//        if ("server".equals(model)) {
//            // 服务端运行模式
//            Server server = new Server();
//            server.run();
//        } else {
//            if (configPath == null || configPath.isEmpty()) {
//                System.out.println("请指定配置文件路径，es: -Djork.client.config=/home/xiaoteng/jork.json");
//                System.exit(2);
//            }
//            Client client = new Client();
//            client.run(configPath);
//        }
    }

}
