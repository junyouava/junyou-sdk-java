package com.junyouava.sdk;

/**
 * 签名信息和 OpenAuth 的组合
 */
public class SignatureWithOpenAuth {
    private String accessId;
    private String signature;
    private String nonce;
    private String timestamp;
    private String openAuth;

    public SignatureWithOpenAuth() {
    }

    public SignatureWithOpenAuth(String accessId, String signature, String nonce, String timestamp, String openAuth) {
        this.accessId = accessId;
        this.signature = signature;
        this.nonce = nonce;
        this.timestamp = timestamp;
        this.openAuth = openAuth;
    }

    public String getAccessId() {
        return accessId;
    }

    public void setAccessId(String accessId) {
        this.accessId = accessId;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getOpenAuth() {
        return openAuth;
    }

    public void setOpenAuth(String openAuth) {
        this.openAuth = openAuth;
    }
}

