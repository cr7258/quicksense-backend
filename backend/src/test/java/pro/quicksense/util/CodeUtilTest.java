package pro.quicksense.util;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CodeUtilTest {

    @Mock
    private StringRedisTemplate redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @InjectMocks
    private CodeUtil codeUtil;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void testGenerateVerificationCode() {
        String code = codeUtil.generateVerificationCode();
        assertNotNull(code);
        assertEquals(6, code.length());
        int codeInt = Integer.parseInt(code);
        assertTrue(codeInt >= 100000 && codeInt <= 999999);
    }

    @Test
    void testVerifyCodeSuccess() {
        String email = "test@example.com";
        String verifyCode = "123456";
        String key = "testKey";

        when(valueOperations.get(key + email)).thenReturn(verifyCode);

        assertTrue(codeUtil.verifyCode(email, verifyCode, key));
        verify(redisTemplate.opsForValue()).get(key + email);
    }

    @Test
    void testVerifyCodeFailure() {
        String email = "test@example.com";
        String verifyCode = "123456";
        String wrongCode = "654321";
        String key = "testKey";

        when(valueOperations.get(key + email)).thenReturn(wrongCode);

        assertFalse(codeUtil.verifyCode(email, verifyCode, key));
        verify(redisTemplate.opsForValue()).get(key + email);
    }
}