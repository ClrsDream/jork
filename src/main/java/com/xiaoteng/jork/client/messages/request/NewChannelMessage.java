package com.xiaoteng.jork.client.messages.request;

import com.alibaba.fastjson.annotation.JSONField;

public class NewChannelMessage {

    @JSONField
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
