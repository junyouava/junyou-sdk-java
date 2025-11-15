package com.junyouava.sdk;

/**
 * 签名信息
 */
public class Signature {
    private String accessId;
    private String signature;
    private String nonce;
    private String timestamp;

    public Signature() {
    }

    public Signature(String accessId, String signature, String nonce, String timestamp) {
        this.accessId = accessId;
        this.signature = signature;
        this.nonce = nonce;
        this.timestamp = timestamp;
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
}

