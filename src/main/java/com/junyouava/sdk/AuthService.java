package com.junyouava.sdk;

import com.junyouava.sdk.model.OpenIdToken;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 认证服务，提供签名和认证 Header 生成功能
 */
public class AuthService {
    private final Config config;
    private APIService apiService;

    public AuthService(Config config) {
        this.config = config;
    }

    /**
     * 设置 API 服务（由 Client 在构造完成后调用）
     */
    void setAPIService(APIService apiService) {
        this.apiService = apiService;
    }

    /**
     * 生成签名
     *
     * @param method HTTP 方法（GET, POST, PUT, DELETE 等）
     * @param path   请求路径
     * @return 签名信息
     * @throws RuntimeException 如果签名生成失败
     */
    public Signature GenerateSignature(String method, String path) {
        try {
            String accessId = config.getAccessId();
            // 当前时间加上3分钟，转换为Unix时间戳（秒）
            String timestamp = String.valueOf(Instant.now().plusSeconds(Constants.TIMESTAMP_OFFSET_SECONDS).getEpochSecond());
            String nonce = UUID.randomUUID().toString().replace("-", "");

            // 构建待签名字符串（格式：AccessId\nmethod\npath\nnonce\ntimestamp，最后没有换行符）
            String signString = buildSignString(accessId, method, path, nonce, timestamp);

            // 使用 HMAC-SHA256 生成签名
            String signature = generateHMACSignature(signString, config.getAccessKey());

            return new Signature(accessId, signature, nonce, timestamp);
        } catch (Exception e) {
            throw new RuntimeException("生成签名失败: " + e.getMessage(), e);
        }
    }

    /**
     * 生成认证 Header
     *
     * @param method HTTP 方法
     * @param path   请求路径
     * @return HTTP Header Map
     * @throws RuntimeException 如果生成失败
     */
    public Map<String, String> GenerateAuthHeader(String method, String path) {
        Signature signature = GenerateSignature(method, path);

        Map<String, String> headers = new HashMap<>();
        headers.put(Constants.HEADER_ACCESS_ID, signature.getAccessId());
        headers.put(Constants.HEADER_SIGNATURE, signature.getSignature());
        headers.put(Constants.HEADER_NONCE, signature.getNonce());
        headers.put(Constants.HEADER_TIMESTAMP, signature.getTimestamp());

        return headers;
    }

    /**
     * 生成签名并调用 AuthCMT，合并返回签名信息和 OpenAuth
     *
     * @param method      HTTP 方法（GET, POST, PUT, DELETE 等）
     * @param path        请求路径
     * @param openIdToken OpenId Token（用于调用 AuthCMT）
     * @return 签名信息和 OpenAuth 的组合
     * @throws RuntimeException 如果生成签名或调用 AuthCMT 失败
     * @throws IllegalStateException 如果 APIService 未初始化
     */
    public SignatureWithOpenAuth GenerateSignatureWithOpenAuth(String method, String path, OpenIdToken openIdToken) {
        if (apiService == null) {
            throw new IllegalStateException("APIService 未初始化，请确保通过 Client 创建 AuthService");
        }
        
        try {
            // 生成签名
            Signature signature = GenerateSignature(method, path);

            // 调用 AuthCMT
            Result<String> result = apiService.AuthCMT(openIdToken);
            if (!result.isSuccess()) {
                throw new RuntimeException("调用 AuthCMT 失败: " + result.getMessage());
            }

            // 合并签名信息和数据
            return new SignatureWithOpenAuth(
                    signature.getAccessId(),
                    signature.getSignature(),
                    signature.getNonce(),
                    signature.getTimestamp(),
                    result.getData()
            );
        } catch (IOException e) {
            throw new RuntimeException("调用 AuthCMT 失败: " + e.getMessage(), e);
        }
    }

    /**
     * 构建待签名字符串
     * 格式：AccessId\nmethod\npath\nnonce\ntimestamp（最后没有换行符）
     * 与 Go SDK 保持一致：fmt.Sprintf("%s\n%s\n%s\n%s\n%s", ...)
     *
     * @param accessId  访问 ID
     * @param method    HTTP 方法
     * @param path      请求路径
     * @param nonce     随机字符串
     * @param timestamp 时间戳
     * @return 待签名字符串
     */
    private String buildSignString(String accessId, String method, String path, String nonce, String timestamp) {
        return accessId + "\n" + method + "\n" + path + "\n" + nonce + "\n" + timestamp;
    }

    /**
     * 使用 HMAC-SHA256 生成签名
     *
     * @param data      待签名数据
     * @param secretKey 密钥（Base64 编码）
     * @return Base64 编码的签名
     * @throws NoSuchAlgorithmException 如果算法不存在
     * @throws InvalidKeyException      如果密钥无效
     */
    private String generateHMACSignature(String data, String secretKey)
            throws NoSuchAlgorithmException, InvalidKeyException {
        try {
            // 解码 Base64 密钥
            byte[] keyBytes = Base64.getDecoder().decode(secretKey);

            // 创建 Mac 实例
            Mac mac = Mac.getInstance(Constants.SIGNATURE_ALGORITHM);
            SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, Constants.SIGNATURE_ALGORITHM);
            mac.init(secretKeySpec);

            // 计算签名
            byte[] signatureBytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));

            // 返回 Base64 编码的签名
            return Base64.getEncoder().encodeToString(signatureBytes);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("AccessKey Base64 解码失败: " + e.getMessage(), e);
        }
    }
}

