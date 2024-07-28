package pro.quicksense.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pro.quicksense.common.CommonConstant;
import pro.quicksense.entity.User;
import pro.quicksense.entity.login.EmailLogin;
import pro.quicksense.entity.login.UsernameLogin;
import pro.quicksense.exception.CustomExceptions;
import pro.quicksense.service.LoginService;
import pro.quicksense.service.UserService;
import pro.quicksense.util.CodeUtil;
import pro.quicksense.util.JwtInterceptor;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private UserService userService;

    @Autowired
    private CodeUtil codeUtil;

    @Autowired
    private JwtInterceptor jwtInterceptor;

    @Override
    public String loginByEmail(EmailLogin emailLogin) {
        User user = userService.findByEmail(emailLogin.getEmail());
        if (user == null) {
            throw new CustomExceptions.LoginException("User does not exist");
        }

        if (!codeUtil.verifyCode(emailLogin.getEmail(), emailLogin.getVerifyCode(), CommonConstant.KEY_PREFIX)) {
            throw new CustomExceptions.LoginException("Incorrect Verification code");
        }

        // Login successful, generate token and return
        return jwtInterceptor.generateToken(user);
    }

    public String loginByUsername(UsernameLogin usernameLogin) {
        User user = userService.findByUsername(usernameLogin.getUsername());
        if (user == null) {
            throw new CustomExceptions.LoginException("Incorrect username or password");
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(usernameLogin.getPassword(), user.getPassword())) {
            throw new CustomExceptions.LoginException("Incorrect username or password");
        }

        if (CommonConstant.USER_STATUS_FROZEN.equals(user.getStatus())) {
            throw new CustomExceptions.LoginException("The user account is deactivate.");
        }

        // Login successful, generate token and return
        return jwtInterceptor.generateToken(user);
    }
}
