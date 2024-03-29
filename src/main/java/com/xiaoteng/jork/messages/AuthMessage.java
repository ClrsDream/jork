package com.xiaoteng.jork.messages;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author xiaoteng
 */
public class AuthMessage {

    @JSONField(name = "username")
    private String username;

    @JSONField(name = "password")
    private String password;

    public AuthMessage(String username, String password) {
        this.username = username;
        this.password = password;
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

    @Override
    public String toString() {
        return "AuthMessage{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
