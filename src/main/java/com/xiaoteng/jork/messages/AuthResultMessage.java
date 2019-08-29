package com.xiaoteng.jork.messages;

import com.alibaba.fastjson.annotation.JSONField;

public class AuthResultMessage {

    @JSONField
    private boolean result;

    public AuthResultMessage(boolean result) {
        this.result = result;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
