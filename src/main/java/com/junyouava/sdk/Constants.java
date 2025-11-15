package com.junyouava.sdk;

/**
 * SDK 常量定义
 */
public class Constants {
    /**
     * 默认 API 服务器地址
     */
    public static final String DEFAULT_ADDRESS = "https://open-api.junyouchain.com";

    /**
     * 默认 API 版本
     */
    public static final String DEFAULT_VERSION = "v1";

    /**
     * 默认内容类型
     */
    public static final String DEFAULT_CONTENT_TYPE = "application/json";

    /**
     * 签名算法
     */
    public static final String SIGNATURE_ALGORITHM = "HmacSHA256";

    /**
     * 认证 Header 名称
     */
    public static final String HEADER_ACCESS_ID = "X-Access-ID";
    public static final String HEADER_SIGNATURE = "X-Signature";
    public static final String HEADER_NONCE = "X-Signature-Nonce";
    public static final String HEADER_TIMESTAMP = "X-Timestamp";

    /**
     * 时间戳偏移量（秒）- 当前时间加上3分钟
     */
    public static final long TIMESTAMP_OFFSET_SECONDS = 180L; // 3分钟 = 180秒

    private Constants() {
        // 工具类，禁止实例化
    }
}

