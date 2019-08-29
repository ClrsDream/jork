package com.xiaoteng.jork.server.main;

import com.alibaba.fastjson.JSON;
import com.xiaoteng.jork.constants.Constants;
import com.xiaoteng.jork.messages.ActionMessage;
import com.xiaoteng.jork.messages.AuthMessage;
import com.xiaoteng.jork.messages.ClientRegisterMessage;
import com.xiaoteng.jork.server.auth.Auth;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

            // 是否登陆
            boolean isLogin = false;

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                log.info("收到消息 {}", line);
                // 客户端发来的消息
                ActionMessage actionMessage = JSON.parseObject(line, ActionMessage.class);
                switch (actionMessage.getMethod()) {
                    case Constants.METHOD_AUTH:
                        AuthMessage authMessage = JSON.parseObject(actionMessage.getContent(), AuthMessage.class);
                        Auth auth = new Auth();
                        if (!auth.check(authMessage.getUsername(), authMessage.getPassword())) {
                            // 登陆失败，主动关闭连接
                            this.socket.close();
                            return;
                        }
                        log.info("登录成功");
                        isLogin = true;
                        break;
                    case Constants.METHOD_REGISTER:
                        if (isLogin) {
                            // 登录之后才可以操作
                            ClientRegisterMessage rm = JSON.parseObject(actionMessage.getContent(), ClientRegisterMessage.class);
                            // 将当前的connection注册到注册表中
                            Client client = new Client(rm.getProtocol(), rm.getPort(), rm.getDomain(), this.socket);
                            log.info("将当前connection注册到注册表中");
                            RegisterTable.register(client.getPort(), client);
                        }
                        break;
                    default:
                        log.warn("{} 行为暂不支持", actionMessage.getMethod());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
