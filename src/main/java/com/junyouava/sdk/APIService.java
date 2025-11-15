package com.junyouava.sdk;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.junyouava.sdk.model.EWTBizNoInfo;
import com.junyouava.sdk.model.OpenIdToken;
import com.junyouava.sdk.model.RegisterInfo;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.util.Map;

/**
 * API 服务，提供所有业务 API 调用
 */
public class APIService {
    private final Config config;
    private final AuthService authService;
    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;

    public APIService(Config config, AuthService authService, OkHttpClient httpClient) {
        this.config = config;
        this.authService = authService;
        this.httpClient = httpClient;
        this.objectMapper = new ObjectMapper();
    }

    /**
     * 注册
     *
     * @param registerInfo 注册信息
     * @return 注册结果
     * @throws IOException 如果请求失败
     */
    public Result<String> Register(RegisterInfo registerInfo) throws IOException {
        String path = "/api/open/" + config.getVersion() + "/register";
        return post(path, registerInfo, String.class);
    }

    /**
     * 登录认证
     *
     * @param openIdToken OpenId Token
     * @return 访问令牌结果
     * @throws IOException 如果请求失败
     */
    public Result<String> AuthLogin(OpenIdToken openIdToken) throws IOException {
        String path = "/api/open/" + config.getVersion() + "/auth/login";
        return post(path, openIdToken, String.class);
    }

    /**
     * 设置密码认证
     *
     * @param openIdToken OpenId Token
     * @return 访问令牌结果
     * @throws IOException 如果请求失败
     */
    public Result<String> AuthSetPWD(OpenIdToken openIdToken) throws IOException {
        String path = "/api/open/" + config.getVersion() + "/auth/setpwd";
        return post(path, openIdToken, String.class);
    }

    /**
     * 验证认证
     *
     * @param openIdToken OpenId Token
     * @return 访问令牌结果
     * @throws IOException 如果请求失败
     */
    public Result<String> AuthCMT(OpenIdToken openIdToken) throws IOException {
        String path = "/api/open/" + config.getVersion() + "/auth/cmt";
        return post(path, openIdToken, String.class);
    }

    /**
     * 确认权证释放
     *
     * @param ewtBizNoInfo 权证业务编号信息
     * @return 确认结果
     * @throws IOException 如果请求失败
     */
    public Result<String> ConfirmEWTReleaseByPartner(EWTBizNoInfo ewtBizNoInfo) throws IOException {
        String path = "/api/open/" + config.getVersion() + "/ewt/confirm-release-by-partner";
        return post(path, ewtBizNoInfo, String.class);
    }

    /**
     * 执行 POST 请求
     *
     * @param path 请求路径
     * @param body 请求体对象
     * @param dataType 响应数据类型
     * @param <T> 响应数据类型
     * @return 响应结果
     * @throws IOException 如果请求失败
     */
    private <T> Result<T> post(String path, Object body, Class<T> dataType) throws IOException {
        // 序列化请求体
        String jsonBody = objectMapper.writeValueAsString(body);
        RequestBody requestBody = RequestBody.create(
                jsonBody,
                MediaType.get(config.getContentType())
        );

        // 生成认证 Header
        Map<String, String> authHeaders = authService.GenerateAuthHeader("POST", path);

        // 构建请求
        Request.Builder requestBuilder = new Request.Builder()
                .url(config.getAddress() + path)
                .post(requestBody)
                .addHeader("Content-Type", config.getContentType());

        // 添加认证 Header
        for (Map.Entry<String, String> entry : authHeaders.entrySet()) {
            requestBuilder.addHeader(entry.getKey(), entry.getValue());
        }

        Request request = requestBuilder.build();

        // 执行请求
        try (Response response = httpClient.newCall(request).execute()) {
            return parseResponse(response, dataType);
        }
    }

    /**
     * 解析响应
     *
     * @param response HTTP 响应
     * @param dataType 响应数据类型
     * @param <T> 响应数据类型
     * @return 解析后的结果
     * @throws IOException 如果解析失败
     */
    private <T> Result<T> parseResponse(Response response, Class<T> dataType) throws IOException {
        Result<T> result = new Result<>();
        result.setCode(response.code());

        String responseBody = response.body() != null ? response.body().string() : "";

        if (response.isSuccessful()) {
            try {
                // 尝试解析 JSON 响应
                // 支持两种格式：{"result":{...}} 或直接 {...}
                Result<T> parsedResult;
                try {
                    // 先尝试解析为包装格式 {"result":{...}}
                    java.util.Map<String, Object> wrapper = objectMapper.readValue(
                            responseBody,
                            java.util.Map.class
                    );
                    if (wrapper.containsKey("result")) {
                        Object resultObj = wrapper.get("result");
                        parsedResult = objectMapper.convertValue(
                                resultObj,
                                objectMapper.getTypeFactory().constructParametricType(Result.class, dataType)
                        );
                    } else {
                        // 直接解析为 Result
                        parsedResult = objectMapper.readValue(
                                responseBody,
                                objectMapper.getTypeFactory().constructParametricType(Result.class, dataType)
                        );
                    }
                } catch (Exception e) {
                    // 如果包装格式解析失败，尝试直接解析
                    parsedResult = objectMapper.readValue(
                            responseBody,
                            objectMapper.getTypeFactory().constructParametricType(Result.class, dataType)
                    );
                }
                
                result.setSuccess(parsedResult.isSuccess());
                result.setCode(parsedResult.getCode());
                result.setErrCode(parsedResult.getErrCode());
                result.setMessage(parsedResult.getMessage());
                result.setData(parsedResult.getData());
            } catch (Exception e) {
                // 如果解析失败，尝试作为字符串处理
                if (dataType == String.class) {
                    result.setSuccess(true);
                    result.setData((T) responseBody);
                } else {
                    result.setSuccess(false);
                    result.setMessage("解析响应失败: " + e.getMessage());
                }
            }
        } else {
            result.setSuccess(false);
            try {
                // 尝试解析错误响应
                // 支持两种格式：{"result":{...}} 或直接 {...}
                Result<?> errorResult;
                try {
                    java.util.Map<String, Object> wrapper = objectMapper.readValue(
                            responseBody,
                            java.util.Map.class
                    );
                    if (wrapper.containsKey("result")) {
                        Object resultObj = wrapper.get("result");
                        errorResult = objectMapper.convertValue(resultObj, Result.class);
                    } else {
                        errorResult = objectMapper.readValue(responseBody, Result.class);
                    }
                } catch (Exception e) {
                    errorResult = objectMapper.readValue(responseBody, Result.class);
                }
                result.setErrCode(errorResult.getErrCode());
                result.setMessage(errorResult.getMessage());
                result.setCode(errorResult.getCode());
            } catch (Exception e) {
                result.setMessage("请求失败: " + responseBody);
            }
        }

        return result;
    }
}

