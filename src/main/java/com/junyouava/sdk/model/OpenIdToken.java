package com.junyouava.sdk.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * OpenId Token 信息
 */
public class OpenIdToken {
    @JsonProperty("open_id")
    private String openId;

    public OpenIdToken() {
    }

    public OpenIdToken(String openId) {
        this.openId = openId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }
}

