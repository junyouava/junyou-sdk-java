package examples;

import com.junyouava.sdk.*;
import com.junyouava.sdk.model.EWTBizNoInfo;
import com.junyouava.sdk.model.OpenIdToken;
import com.junyouava.sdk.model.RegisterInfo;

import java.io.IOException;

/**
 * 基础使用示例
 */
public class BasicExample {
    public static void main(String[] args) {
        // 初始化客户端
        Config config = Config.DefaultConfig()
                .WithAccessId("your-access-id")
                .WithAccessKey("your-access-key");

        Client client = Client.NewClient(config);
        
        try {
            // 示例1: 注册
            try {
                RegisterInfo registerInfo = new RegisterInfo("13800138000");
                Result<String> result = client.API().Register(registerInfo);

                if (result.isSuccess()) {
                    System.out.println("注册成功: " + result.getData());
                } else {
                    System.out.println("注册失败: " + result.getMessage());
                }
            } catch (IOException e) {
                System.err.println("注册请求失败: " + e.getMessage());
            }

            // 示例2: 获取登录令牌
            try {
                OpenIdToken openIdToken = new OpenIdToken("user-open-id");
                Result<String> result = client.API().AuthLogin(openIdToken);

                if (result.isSuccess()) {
                    String accessToken = result.getData();
                    System.out.println("Access Token: " + accessToken);
                } else {
                    System.out.println("获取令牌失败: " + result.getMessage());
                }
            } catch (IOException e) {
                System.err.println("获取令牌请求失败: " + e.getMessage());
            }

            // 示例3: 获取设置密码令牌
            try {
                OpenIdToken openIdToken = new OpenIdToken("user-open-id");
                Result<String> result = client.API().AuthSetPWD(openIdToken);

                if (result.isSuccess()) {
                    String accessToken = result.getData();
                    System.out.println("Access Token: " + accessToken);
                } else {
                    System.out.println("获取令牌失败: " + result.getMessage());
                }
            } catch (IOException e) {
                System.err.println("获取令牌请求失败: " + e.getMessage());
            }

            // 示例4: 获取验证令牌
            try {
                OpenIdToken openIdToken = new OpenIdToken("user-open-id");
                Result<String> result = client.API().AuthCMT(openIdToken);

                if (result.isSuccess()) {
                    String accessToken = result.getData();
                    System.out.println("Access Token: " + accessToken);
                } else {
                    System.out.println("获取令牌失败: " + result.getMessage());
                }
            } catch (IOException e) {
                System.err.println("获取令牌请求失败: " + e.getMessage());
            }

            // 示例5: 释放权证
            try {
                EWTBizNoInfo ewtBizNoInfo = new EWTBizNoInfo("ewt-biz-no");
                Result<String> result = client.API().ConfirmEWTReleaseByPartner(ewtBizNoInfo);

                if (result.isSuccess()) {
                    System.out.println("释放权证成功");
                } else {
                    System.out.println("释放权证失败: " + result.getMessage());
                }
            } catch (IOException e) {
                System.err.println("释放权证请求失败: " + e.getMessage());
            }

            // 示例6: 生成签名
            try {
                Signature signature = client.Auth().GenerateSignature("POST", "/api/open/v1/register");
                System.out.println("AccessId: " + signature.getAccessId());
                System.out.println("Signature: " + signature.getSignature());
                System.out.println("Nonce: " + signature.getNonce());
                System.out.println("Timestamp: " + signature.getTimestamp());
            } catch (Exception e) {
                System.err.println("生成签名失败: " + e.getMessage());
            }

            // 示例7: 生成认证 Header
            try {
                java.util.Map<String, String> headers = client.Auth().GenerateAuthHeader("POST", "/api/open/v1/register");
                System.out.println("认证 Header:");
                headers.forEach((key, value) -> System.out.println("  " + key + ": " + value));
            } catch (Exception e) {
                System.err.println("生成认证 Header 失败: " + e.getMessage());
            }
        } finally {
            // 关闭客户端，释放资源
            client.Close();
            // 强制退出，避免 Maven exec 插件等待线程关闭
            System.exit(0);
        }
    }
}

