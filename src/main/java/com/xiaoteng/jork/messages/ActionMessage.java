package com.xiaoteng.jork.messages;

import com.alibaba.fastjson.annotation.JSONField;

public class ActionMessage {

    @JSONField
    private String method;

    @JSONField
    private String content;

    public ActionMessage(String method, String content) {
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
