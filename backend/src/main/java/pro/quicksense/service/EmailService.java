package pro.quicksense.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import pro.quicksense.common.Constant;
import pro.quicksense.util.CodeUtil;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private CodeUtil codeUtil;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${app.email.expiration}")
    private int expiration;

    public void sendVerificationCode(String verifyType, String email) {
        String subject;
        String content;

        switch (verifyType) {
            case Constant.EMAIL_LOGIN:
                String verificationCode = codeUtil.generateVerificationCode();
                content = String.format(
                        "Welcome to QuickSense!\n\n" +
                                "Your login verification code is: %s\n\n" +
                                "Please keep it safe.\n\n" +
                                "This code will expire in %d minutes.",
                        verificationCode, expiration
                );
                subject = "QuickSense Email Login Verification";

                break;
            // TODO: extend the switch statement to handle more email types, for example: recover password, change email, etc.
            default:
                log.error("Invalid email verification code type: {}", verifyType);
                throw new IllegalArgumentException("Invalid email type");
        }
        sendEmail(email, subject, content);
    }
    
    public void sendEmail(String email, String subject, String content) {
        if (isInvalidEmail(email)) {
            log.error("Invalid email address: {}", email);
            throw new IllegalArgumentException("Invalid email address");
        }

        String verificationCode = codeUtil.generateVerificationCode();
        String key = Constant.EMAIL_LOGIN + email;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(username);
        message.setTo(email);
        message.setSubject(subject);
        message.setText(content);

        try {
            mailSender.send(message);
            redisTemplate.opsForValue().set(key, verificationCode, expiration, TimeUnit.MINUTES);
            log.info("Verification code sent to: {}", email);
        } catch (Exception e) {
            log.error("Failed to send email to: {}", email, e);
            throw new RuntimeException("Failed to send email", e);
        }
    }

    public boolean isInvalidEmail(String email) {
        String emailPattern = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
        return !email.matches(emailPattern);
    }
}
