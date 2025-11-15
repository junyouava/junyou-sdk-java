package com.junyouava.sdk;

import okhttp3.OkHttpClient;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * SDK 主客户端，提供所有服务访问入口
 */
public class Client {
    private final Config config;
    private final OkHttpClient httpClient;
    private final AuthService authService;
    private final APIService apiService;

    /**
     * 创建新客户端（会验证配置）
     *
     * @param config 配置对象
     * @return 客户端实例
     * @throws IllegalArgumentException 如果配置无效
     */
    public static Client NewClient(Config config) {
        return NewClientWithHTTPClient(config, null);
    }

    /**
     * 使用自定义 HTTP 客户端创建客户端（会验证配置）
     *
     * @param config     配置对象
     * @param httpClient 自定义 HTTP 客户端（如果为 null，则使用默认客户端）
     * @return 客户端实例
     * @throws IllegalArgumentException 如果配置无效
     */
    public static Client NewClientWithHTTPClient(Config config, OkHttpClient httpClient) {
        // 验证配置
        config.validate();

        // 如果没有提供 HTTP 客户端，使用默认的
        if (httpClient == null) {
            httpClient = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build();
        }

        return new Client(config, httpClient);
    }

    private Client(Config config, OkHttpClient httpClient) {
        this.config = config;
        this.httpClient = httpClient;
        this.authService = new AuthService(config);
        this.apiService = new APIService(config, authService, httpClient);
    }

    /**
     * 获取配置
     *
     * @return 配置对象
     */
    public Config getConfig() {
        return config;
    }

    /**
     * 获取 HTTP 客户端
     *
     * @return HTTP 客户端
     */
    public OkHttpClient getHttpClient() {
        return httpClient;
    }

    /**
     * 获取认证服务
     *
     * @return 认证服务
     */
    public AuthService Auth() {
        return authService;
    }

    /**
     * 获取 API 服务
     *
     * @return API 服务
     */
    public APIService API() {
        return apiService;
    }

    /**
     * 关闭客户端，释放资源
     * 关闭 OkHttp 的调度线程池、连接池和缓存
     * 注意：此方法不会等待线程池完全关闭，线程会在后台异步关闭
     */
    public void Close() {
        if (httpClient == null) {
            return;
        }

        // 关闭调度线程池（异步关闭，不等待）
        ExecutorService executorService = httpClient.dispatcher().executorService();
        executorService.shutdown();

        // 清理连接池
        httpClient.connectionPool().evictAll();

        // 关闭缓存（如果存在）
        if (httpClient.cache() != null) {
            try {
                httpClient.cache().close();
            } catch (IOException e) {
                // 忽略缓存关闭异常，不影响主流程
            }
        }
    }
}

