package com.xiaoteng.jork.server.messages.response;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author xiaoteng
 */
public class NewChannelMessage {

    @JSONField
    private Integer id;

    @JSONField
    private String method;

    public NewChannelMessage(Integer id) {
        this.id = id;
        this.method = "channel";
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
