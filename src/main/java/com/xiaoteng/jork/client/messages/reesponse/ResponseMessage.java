package com.xiaoteng.jork.client.messages.reesponse;

import com.alibaba.fastjson.annotation.JSONField;

public class ResponseMessage {

    @JSONField
    private String method;

    @JSONField
    private String content;

    public ResponseMessage(String method, String content) {
        this.method = method;
        this.content = content;
    }

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
