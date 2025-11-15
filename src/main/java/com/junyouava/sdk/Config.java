package com.junyouava.sdk;

/**
 * SDK 配置类
 */
public class Config {
    /**
     * 访问 ID（必需）
     */
    private String accessId;

    /**
     * 访问密钥（必需，Base64 编码）
     */
    private String accessKey;

    /**
     * API 版本（可选，默认 "v1"）
     */
    private String version = Constants.DEFAULT_VERSION;

    /**
     * API 服务器地址（可选，默认 "https://open-api.junyouchain.com"）
     */
    private String address = Constants.DEFAULT_ADDRESS;

    /**
     * 请求内容类型（可选，默认 "application/json"）
     */
    private String contentType = Constants.DEFAULT_CONTENT_TYPE;

    /**
     * 创建默认配置
     *
     * @return 配置对象
     */
    public static Config DefaultConfig() {
        return new Config();
    }

    /**
     * 设置 AccessId
     *
     * @param accessId 访问 ID
     * @return 当前配置对象（支持链式调用）
     */
    public Config WithAccessId(String accessId) {
        this.accessId = accessId;
        return this;
    }

    /**
     * 设置 AccessKey
     *
     * @param accessKey 访问密钥（Base64 编码）
     * @return 当前配置对象（支持链式调用）
     */
    public Config WithAccessKey(String accessKey) {
        this.accessKey = accessKey;
        return this;
    }

    /**
     * 设置版本
     *
     * @param version API 版本
     * @return 当前配置对象（支持链式调用）
     */
    public Config WithVersion(String version) {
        this.version = version;
        return this;
    }

    /**
     * 设置服务器地址
     *
     * @param address API 服务器地址
     * @return 当前配置对象（支持链式调用）
     */
    public Config WithAddress(String address) {
        this.address = address;
        return this;
    }

    /**
     * 设置内容类型
     *
     * @param contentType 请求内容类型
     * @return 当前配置对象（支持链式调用）
     */
    public Config WithContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    /**
     * 验证配置是否有效
     *
     * @throws IllegalArgumentException 如果配置无效
     */
    public void validate() {
        if (accessId == null || accessId.trim().isEmpty()) {
            throw new IllegalArgumentException("AccessId 不能为空");
        }
        if (accessKey == null || accessKey.trim().isEmpty()) {
            throw new IllegalArgumentException("AccessKey 不能为空");
        }
    }

    // Getters
    public String getAccessId() {
        return accessId;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public String getVersion() {
        return version;
    }

    public String getAddress() {
        return address;
    }

    public String getContentType() {
        return contentType;
    }
}

