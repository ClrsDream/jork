package com.xiaoteng.jork.server.messages.request;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author xiaoteng
 */
public class RegisterMessage {

    @JSONField(name = "domain")
    private String domain;

    @JSONField(name = "port")
    private Integer port;

    @JSONField(name = "protocol")
    private String protocol;

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }
}
