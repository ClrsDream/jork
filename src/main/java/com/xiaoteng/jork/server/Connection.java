package com.xiaoteng.jork.server;

import com.alibaba.fastjson.JSON;
import com.xiaoteng.jork.server.messages.request.ActionMessage;
import com.xiaoteng.jork.server.messages.request.AuthMessage;
import com.xiaoteng.jork.server.messages.request.RegisterMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;

/**
 * @author xiaoteng
 */
public class Connection implements Runnable {

    private final static Logger log = LogManager.getLogger(Connection.class);

    private Socket socket;

    Connection(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        InputStream inputStream = null;
        try {
            inputStream = this.socket.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            while (true) {
                String line = bufferedReader.readLine();
                if (!line.isEmpty()) {
                    // 客户端发来的消息
                    ActionMessage actionMessage = JSON.parseObject(line, ActionMessage.class);
                    switch (actionMessage.getActionMethod()) {
                        case "auth":
                            AuthMessage authMessage = JSON.parseObject(actionMessage.getContent(), AuthMessage.class);
                            Auth auth = new Auth();
                            if (!auth.check(authMessage.getUsername(), authMessage.getPassword())) {
                                // 登陆失败，主动关闭连接
                                this.socket.close();
                                return;
                            }

                            break;
                        case "register":
                            RegisterMessage registerMessage = JSON.parseObject(actionMessage.getContent(), RegisterMessage.class);
                            break;
                        default:
                            log.warn("{} 行为不支持", actionMessage.getActionMethod());
                    }
                }
                // 暂停0.5s
                Thread.sleep(500);
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
