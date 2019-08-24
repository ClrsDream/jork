package com.xiaoteng.jork.server.messages.request;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author xiaoteng
 */
public class ActionMessage {

    @JSONField(name = "action_method")
    private String actionMethod;

    @JSONField(name = "content")
    private String content;

    public String getActionMethod() {
        return actionMethod;
    }

    public void setActionMethod(String actionMethod) {
        this.actionMethod = actionMethod;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
