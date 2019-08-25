package com.xiaoteng.jork.client.main.config;

import com.alibaba.fastjson.annotation.JSONField;

public class Config {

    @JSONField
    private int port;

    @JSONField
    private String address;

    @JSONField
    private String domain;

    @JSONField
    private String[] protocol;

    @JSONField
    private String username;

    @JSONField
    private String password;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String[] getProtocol() {
        return protocol;
    }

    public void setProtocol(String[] protocol) {
        this.protocol = protocol;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
