package com.xiaoteng.jork.client.messages.request;

import com.alibaba.fastjson.annotation.JSONField;

public class RequestMessage {

    @JSONField
    private String method;

    @JSONField
    private String content;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
