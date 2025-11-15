package com.junyouava.sdk;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 客户端测试类
 */
public class ClientTest {

    @Test
    public void testConfigValidation() {
        // 测试空 AccessId
        assertThrows(IllegalArgumentException.class, () -> {
            Config config = Config.DefaultConfig()
                    .WithAccessKey("test-key");
            config.validate();
        });

        // 测试空 AccessKey
        assertThrows(IllegalArgumentException.class, () -> {
            Config config = Config.DefaultConfig()
                    .WithAccessId("test-id");
            config.validate();
        });

        // 测试有效配置
        assertDoesNotThrow(() -> {
            Config config = Config.DefaultConfig()
                    .WithAccessId("test-id")
                    .WithAccessKey("test-key");
            config.validate();
        });
    }

    @Test
    public void testClientCreation() {
        Config config = Config.DefaultConfig()
                .WithAccessId("test-id")
                .WithAccessKey("dGVzdC1rZXk="); // Base64 encoded "test-key"

        Client client = Client.NewClient(config);
        assertNotNull(client);
        assertNotNull(client.getConfig());
        assertNotNull(client.getHttpClient());
        assertNotNull(client.Auth());
        assertNotNull(client.API());
    }

    @Test
    public void testSignatureGeneration() {
        Config config = Config.DefaultConfig()
                .WithAccessId("test-id")
                .WithAccessKey("dGVzdC1rZXk="); // Base64 encoded "test-key"

        Client client = Client.NewClient(config);
        Signature signature = client.Auth().GenerateSignature("POST", "/api/open/v1/register");

        assertNotNull(signature);
        assertNotNull(signature.getAccessId());
        assertNotNull(signature.getSignature());
        assertNotNull(signature.getNonce());
        assertNotNull(signature.getTimestamp());
        assertEquals("test-id", signature.getAccessId());
    }
}

