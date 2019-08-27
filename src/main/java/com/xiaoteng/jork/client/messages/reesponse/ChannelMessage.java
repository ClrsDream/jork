package com.xiaoteng.jork.client.messages.reesponse;

import com.alibaba.fastjson.annotation.JSONField;

public class ChannelMessage {

    @JSONField
    private int id;

    public ChannelMessage(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
