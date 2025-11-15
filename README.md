# Junyou SDK Java

Junyou SDK，提供简洁易用的 API 接口。

## 功能特性

* 🔐 **安全认证**：支持 HMAC-SHA256 签名算法，自动生成认证 Header
* 📝 **用户注册**：提供用户注册接口，支持手机号注册
* 🔑 **多种认证方式**：支持登录认证、设置密码认证、验证认证等多种令牌获取方式
* 🎫 **权证管理**：支持权证（EWT）的释放确认操作
* ⚙️ **灵活配置**：支持自定义配置，包括 API 地址、版本、内容类型等
* 🔧 **自定义 HTTP 客户端**：支持使用自定义 HTTP 客户端，方便集成到现有项目
* 📦 **类型安全**：使用 Java 泛型，提供类型安全的 API 响应处理
* 🛡️ **完善的错误处理**：区分网络错误和业务错误，提供详细的错误信息

## 要求

* **Java 版本**：>= 11
* **Maven**：>= 3.6

### 依赖库

本 SDK 使用以下依赖库（已包含在 SDK 中，会通过 Maven/Gradle 自动传递依赖）：

* **OkHttp 4.11.0**：HTTP 客户端
* **Jackson 2.15.2**：JSON 处理

**注意**：使用者无需显式声明这些依赖，Maven/Gradle 会自动处理传递依赖。如果您的项目中已有这些依赖的不同版本，可能会发生版本冲突，建议统一版本或排除传递依赖。

## 安装

### Maven

在 `pom.xml` 中添加依赖：

```xml
<dependency>
    <groupId>com.junyouava</groupId>
    <artifactId>junyou-sdk-java</artifactId>
    <version>1.0.0</version>
</dependency>
```

<details>
<summary>如果遇到版本冲突，点击查看解决方案</summary>

可以排除传递依赖并显式声明您需要的版本：

```xml
<dependency>
    <groupId>com.junyouava</groupId>
    <artifactId>junyou-sdk-java</artifactId>
    <version>1.0.0</version>
    <exclusions>
        <exclusion>
            <groupId>com.squareup.okhttp3</groupId>
            <artifactId>okhttp</artifactId>
        </exclusion>
        <exclusion>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
        </exclusion>
    </exclusions>
</dependency>
<!-- 然后显式声明您需要的版本 -->
<dependency>
    <groupId>com.squareup.okhttp3</groupId>
    <artifactId>okhttp</artifactId>
    <version>4.11.0</version>
</dependency>
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.15.2</version>
</dependency>
```

</details>

### Gradle

在 `build.gradle` 中添加依赖：

```gradle
dependencies {
    implementation 'com.junyouava:junyou-sdk-java:1.0.0'
}
```

<details>
<summary>如果遇到版本冲突，点击查看解决方案</summary>

可以排除传递依赖并显式声明您需要的版本：

```gradle
dependencies {
    implementation('com.junyouava:junyou-sdk-java:1.0.0') {
        exclude group: 'com.squareup.okhttp3', module: 'okhttp'
        exclude group: 'com.fasterxml.jackson.core', module: 'jackson-databind'
    }
    // 然后显式声明您需要的版本
    implementation 'com.squareup.okhttp3:okhttp:4.11.0'
    implementation 'com.fasterxml.jackson.core:jackson-databind:2.15.2'
}
```

</details>

## 快速开始

### 初始化客户端

```java
import com.junyouava.sdk.*;
import okhttp3.OkHttpClient;
import java.util.concurrent.TimeUnit;

// 方式1: 使用默认配置
Config config = Config.DefaultConfig()
    .WithAccessId("your-access-id")
    .WithAccessKey("your-access-key");

Client client = Client.NewClient(config);

// 方式2: 直接创建配置
Client client = Client.NewClient(new Config()
    .WithAccessId("your-access-id")
    .WithAccessKey("your-access-key"));

// 方式3: 使用自定义 HTTP 客户端
OkHttpClient httpClient = new OkHttpClient.Builder()
    .connectTimeout(30, TimeUnit.SECONDS)
    .readTimeout(30, TimeUnit.SECONDS)
    .build();

Client client = Client.NewClientWithHTTPClient(config, httpClient);
```

**注意**：`AccessId` 和 `AccessKey` 需要从 Junyou 平台获取，请妥善保管您的密钥信息。

### 注册

```java
import com.junyouava.sdk.model.RegisterInfo;

RegisterInfo registerInfo = new RegisterInfo("13800138000");

Result<String> result = client.API().Register(registerInfo);
if (result.isSuccess()) {
    System.out.println("注册成功: " + result.getData());
} else {
    System.out.println("注册失败: " + result.getMessage());
}
```

### 获取认证令牌

SDK 支持多种认证方式，根据业务场景选择相应的方法：

```java
import com.junyouava.sdk.model.OpenIdToken;

OpenIdToken openIdToken = new OpenIdToken("user-open-id");

// 登录认证
Result<String> loginResult = client.API().AuthLogin(openIdToken);

// 设置密码认证
Result<String> setPwdResult = client.API().AuthSetPWD(openIdToken);

// 验证认证
Result<String> cmtResult = client.API().AuthCMT(openIdToken);

// 处理结果
if (loginResult.isSuccess()) {
    String accessToken = loginResult.getData();
    System.out.println("Access Token: " + accessToken);
} else {
    System.out.println("获取令牌失败: " + loginResult.getMessage());
}
```

### 释放权证

```java
import com.junyouava.sdk.model.EWTBizNoInfo;

EWTBizNoInfo ewtBizNoInfo = new EWTBizNoInfo("ewt-biz-no");

Result<String> result = client.API().ConfirmEWTReleaseByPartner(ewtBizNoInfo);
if (result.isSuccess()) {
    System.out.println("释放权证成功");
} else {
    System.out.println("释放权证失败: " + result.getMessage());
}
```

### 高级用法

#### 生成签名和认证 Header

如果需要手动构建 HTTP 请求，可以使用认证服务生成签名和 Header：

```java
import java.util.Map;

// 生成签名
Signature signature = client.Auth().GenerateSignature("POST", "/api/open/v1/register");

// 或直接生成认证 Header（推荐）
Map<String, String> headers = client.Auth().GenerateAuthHeader("POST", "/api/open/v1/register");
// headers 可以直接用于 HTTP 请求
```

## API 参考

详细的 API 文档请参考 [Junyou SDK](https://docs.junyouchain.com/)。

### 主要类

* **`Client`** - SDK 主客户端，提供所有服务访问入口
* **`Config`** - 配置类，支持链式调用
* **`AuthService`** - 认证服务，提供签名和认证 Header 生成
* **`APIService`** - API 服务，提供所有业务 API 调用
* **`Result<T>`** - 泛型响应结果类

### 主要方法

* `Client.NewClient(Config)` - 创建客户端
* `Client.NewClientWithHTTPClient(Config, OkHttpClient)` - 使用自定义 HTTP 客户端创建客户端
* `client.API().Register(RegisterInfo)` - 用户注册
* `client.API().AuthLogin(OpenIdToken)` - 登录认证
* `client.API().AuthSetPWD(OpenIdToken)` - 设置密码认证
* `client.API().AuthCMT(OpenIdToken)` - 验证认证
* `client.API().ConfirmEWTReleaseByPartner(EWTBizNoInfo)` - 确认权证释放
* `client.Auth().GenerateSignature(String, String)` - 生成签名
* `client.Auth().GenerateAuthHeader(String, String)` - 生成认证 Header

## 配置

```java
Config config = Config.DefaultConfig()
    .WithAccessId("your-access-id")        // 访问 ID（必需）
    .WithAccessKey("your-access-key")      // 访问密钥（必需，Base64 编码）
    .WithVersion("v1")                      // API 版本（可选，默认 "v1"）
    .WithAddress("https://open-sdk.junyouchain.com")  // API 服务器地址（可选）
    .WithContentType("application/json");   // 请求内容类型（可选，默认 "application/json"）
```

## 错误处理

### 异常类型

SDK 可能抛出以下异常：

* `IllegalArgumentException` - 配置无效时抛出（如 AccessId 或 AccessKey 为空）
* `IOException` - 网络请求失败或响应解析失败时抛出
* `RuntimeException` - 签名生成失败时抛出（通常由配置错误引起）

### 结果处理

所有 API 方法都返回 `Result<T>` 和可能抛出 `IOException`。

* `Result.isSuccess()` - 表示请求是否成功
* `Result.getCode()` - HTTP 状态码或业务状态码
* `Result.getErrCode()` - 业务错误代码（字符串）
* `Result.getMessage()` - 错误或成功消息
* `Result.getData()` - 响应数据

### 完整示例

```java
import java.io.IOException;

try {
    Result<String> result = client.API().Register(registerInfo);
    
    if (!result.isSuccess()) {
        // 业务错误
        if (result.getErrCode() != null && !result.getErrCode().isEmpty()) {
            System.out.printf("错误: %s (错误代码: %s, 状态码: %d)%n",
                result.getMessage(), result.getErrCode(), result.getCode());
        } else {
            System.out.printf("错误: %s (状态码: %d)%n",
                result.getMessage(), result.getCode());
        }
        return;
    }
    
    // 成功
    System.out.println("成功: " + result.getData());
} catch (IllegalArgumentException e) {
    // 配置错误
    System.err.println("配置错误: " + e.getMessage());
} catch (IOException e) {
    // 网络错误或其他系统错误
    System.err.println("请求失败: " + e.getMessage());
    e.printStackTrace();
}
```

## 框架集成

### Spring Boot / Spring Framework

```java
@Configuration
public class JunyouSDKConfig {
    
    @Value("${junyou.access-id}")
    private String accessId;
    
    @Value("${junyou.access-key}")
    private String accessKey;
    
    @Bean
    public Client junyouClient() {
        return Client.NewClient(Config.DefaultConfig()
            .WithAccessId(accessId)
            .WithAccessKey(accessKey));
    }
}

@Service
public class UserService {
    @Autowired
    private Client junyouClient;
    
    public void registerUser(String phoneNumber) throws IOException {
        RegisterInfo registerInfo = new RegisterInfo(phoneNumber);
        Result<String> result = junyouClient.API().Register(registerInfo);
        // 处理结果...
    }
}
```

## 许可证

MIT License

## 常见问题

### 如何获取 AccessId 和 AccessKey？

AccessId 和 AccessKey 需要从 Junyou 平台申请获取，请联系平台管理员或查看平台文档。

### AccessKey 需要 Base64 编码吗？

是的，AccessKey 必须是 Base64 编码的字符串。如果您的密钥不是 Base64 格式，请先进行编码。

### 如何调试请求？

您可以通过自定义 HTTP 客户端来添加日志拦截器，例如使用 OkHttp 的 `HttpLoggingInterceptor`：

```java
import okhttp3.logging.HttpLoggingInterceptor;

HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
logging.setLevel(HttpLoggingInterceptor.Level.BODY);

OkHttpClient httpClient = new OkHttpClient.Builder()
    .addInterceptor(logging)
    .build();

Client client = Client.NewClientWithHTTPClient(config, httpClient);
```

## 许可证

MIT License

