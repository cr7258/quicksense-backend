package pro.quicksense.util;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;
import pro.quicksense.integration.AbstractIntegrationTest;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
class CodeUtilIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private CodeUtil codeUtil;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    void testVerifyCodeWithRealRedis() {
        String email = "test@example.com";
        String verifyCode = "123456";
        String key = "testKey";

        // Store the code in Redis
        redisTemplate.opsForValue().set(key + email, verifyCode);

        // Verify the code
        assertTrue(codeUtil.verifyCode(email, verifyCode, key), "Verification should succeed with correct code");
        assertFalse(codeUtil.verifyCode(email, "777777", key), "Verification should fail with wrong code");
    }
}