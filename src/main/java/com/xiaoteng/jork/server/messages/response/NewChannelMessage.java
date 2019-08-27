package com.xiaoteng.jork.server.messages.response;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author xiaoteng
 */
public class NewChannelMessage {

    @JSONField
    private Integer id;

    public NewChannelMessage(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
