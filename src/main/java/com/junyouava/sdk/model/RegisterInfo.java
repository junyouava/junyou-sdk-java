package com.junyouava.sdk.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 注册信息
 */
public class RegisterInfo {
    @JsonProperty("phone_number")
    private String phoneNumber;

    public RegisterInfo() {
    }

    public RegisterInfo(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}

