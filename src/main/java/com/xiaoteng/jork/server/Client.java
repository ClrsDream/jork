package com.xiaoteng.jork.server;

import java.net.Socket;

/**
 * 客户端
 *
 * @author xiaoteng
 */
public class Client {

    private String protocol;

    private Integer port;

    private String domain;

    private Socket socket;

    public Client(String protocol, Integer port, String domain, Socket socket) {
        this.protocol = protocol;
        this.port = port;
        this.domain = domain;
        this.socket = socket;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
}
