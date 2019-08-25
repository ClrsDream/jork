package com.xiaoteng.jork.server.messages.request;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author xiaoteng
 */
public class NewChannelMessage {

    @JSONField
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
