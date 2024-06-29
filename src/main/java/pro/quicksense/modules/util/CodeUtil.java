package pro.quicksense.modules.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class CodeUtil {

    @Autowired
    private StringRedisTemplate redisTemplate;

    public String generateVerificationCode() {
        return String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
    }

    public boolean verifyCode(String email, String verifyCode,String key) {
        key = key + email;
        String storedCode = redisTemplate.opsForValue().get(key);
        return verifyCode.equals(storedCode);
    }

    public void deleteCode(String userId,String key) {
        key = key + userId;
        redisTemplate.delete(key);
    }
}

