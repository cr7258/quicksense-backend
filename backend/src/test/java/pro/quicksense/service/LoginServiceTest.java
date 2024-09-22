package pro.quicksense.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pro.quicksense.common.Constant;
import pro.quicksense.entity.User;
import pro.quicksense.entity.login.EmailLogin;
import pro.quicksense.entity.login.UsernameLogin;
import pro.quicksense.exception.CustomExceptions;
import pro.quicksense.util.CodeUtil;
import pro.quicksense.util.JwtInterceptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class LoginServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private CodeUtil codeUtil;

    @Mock
    private JwtInterceptor jwtInterceptor;

    @InjectMocks
    private LoginService loginService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoginByEmailSuccess() {
        EmailLogin emailLogin = new EmailLogin();
        emailLogin.setEmail("test@example.com");
        emailLogin.setVerifyCode("123456");

        User user = new User();
        user.setEmail("test@example.com");

        when(userService.findByEmail(anyString())).thenReturn(user);
        when(codeUtil.verifyCode(anyString(), anyString(), anyString())).thenReturn(true);
        when(jwtInterceptor.generateToken(any(User.class))).thenReturn("mockToken");

        String token = loginService.loginByEmail(emailLogin);

        assertEquals("mockToken", token);
        verify(userService).findByEmail("test@example.com");
        verify(codeUtil).verifyCode("test@example.com", "123456", Constant.EMAIL_LOGIN);
        verify(jwtInterceptor).generateToken(user);
    }

    @Test
    void testLoginByEmailUserNotFound() {
        EmailLogin emailLogin = new EmailLogin();
        emailLogin.setEmail("test@example.com");
        emailLogin.setVerifyCode("123456");

        when(userService.findByEmail(anyString())).thenReturn(null);

        CustomExceptions.LoginException thrown = assertThrows(CustomExceptions.LoginException.class,
                () -> loginService.loginByEmail(emailLogin));

        assertEquals("User does not exist", thrown.getMessage());
        verify(userService).findByEmail("test@example.com");
        verify(codeUtil, never()).verifyCode(anyString(), anyString(), anyString());
        verify(jwtInterceptor, never()).generateToken(any(User.class));
    }

    @Test
    void testLoginByEmailIncorrectVerificationCode() {
        EmailLogin emailLogin = new EmailLogin();
        emailLogin.setEmail("test@example.com");
        emailLogin.setVerifyCode("123456");

        User user = new User();
        user.setEmail("test@example.com");

        when(userService.findByEmail(anyString())).thenReturn(user);
        when(codeUtil.verifyCode(anyString(), anyString(), anyString())).thenReturn(false);

        CustomExceptions.LoginException thrown = assertThrows(CustomExceptions.LoginException.class,
                () -> loginService.loginByEmail(emailLogin));

        assertEquals("Incorrect Verification code", thrown.getMessage());
        verify(userService).findByEmail("test@example.com");
        verify(codeUtil).verifyCode("test@example.com", "123456", Constant.EMAIL_LOGIN);
        verify(jwtInterceptor, never()).generateToken(any(User.class));
    }

    @Test
    void testLoginByUsernameSuccess() {
        UsernameLogin usernameLogin = new UsernameLogin();
        usernameLogin.setUsername("testUser");
        usernameLogin.setPassword("plainPassword");

        User user = new User();
        user.setUsername("testUser");
        user.setPassword(new BCryptPasswordEncoder().encode("plainPassword"));
        user.setStatus(1);

        when(userService.findByUsername(anyString())).thenReturn(user);
        when(jwtInterceptor.generateToken(any(User.class))).thenReturn("mockToken");

        String token = loginService.loginByUsername(usernameLogin);

        assertEquals("mockToken", token);
        verify(userService).findByUsername("testUser");
        verify(jwtInterceptor).generateToken(user);
    }

    @Test
    void testLoginByUsernameIncorrectUsernameOrPassword() {
        UsernameLogin usernameLogin = new UsernameLogin();
        usernameLogin.setUsername("testUser");
        usernameLogin.setPassword("plainPassword");

        when(userService.findByUsername(anyString())).thenReturn(null);

        CustomExceptions.LoginException thrown = assertThrows(CustomExceptions.LoginException.class,
                () -> loginService.loginByUsername(usernameLogin));

        assertEquals("Incorrect username or password", thrown.getMessage());
        verify(userService).findByUsername("testUser");
        verify(jwtInterceptor, never()).generateToken(any(User.class));
    }

    @Test
    void testLoginByUsernameUserStatusFrozen() {
        UsernameLogin usernameLogin = new UsernameLogin();
        usernameLogin.setUsername("testUser");
        usernameLogin.setPassword("plainPassword");

        User user = new User();
        user.setUsername("testUser");
        user.setPassword(new BCryptPasswordEncoder().encode("plainPassword"));
        user.setStatus(Constant.USER_STATUS_FROZEN);

        when(userService.findByUsername(anyString())).thenReturn(user);

        CustomExceptions.LoginException thrown = assertThrows(CustomExceptions.LoginException.class,
                () -> loginService.loginByUsername(usernameLogin));

        assertEquals("The user account is deactivate.", thrown.getMessage());
        verify(userService).findByUsername("testUser");
        verify(jwtInterceptor, never()).generateToken(any(User.class));
    }
}
