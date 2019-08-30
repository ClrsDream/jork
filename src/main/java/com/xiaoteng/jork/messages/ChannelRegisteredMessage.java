package com.xiaoteng.jork.messages;

import com.alibaba.fastjson.annotation.JSONField;

public class ChannelRegisteredMessage {

    @JSONField
    private int id;

    public ChannelRegisteredMessage(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ChannelRegisteredMessage{" +
                "id=" + id +
                '}';
    }
}
