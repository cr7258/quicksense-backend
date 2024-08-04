package pro.quicksense.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import pro.quicksense.config.TestSecurityConfig;
import pro.quicksense.entity.login.EmailLogin;
import pro.quicksense.entity.login.UsernameLogin;
import pro.quicksense.exception.CustomExceptions;
import pro.quicksense.service.LoginService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LoginController.class)
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoginService loginService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void loginByUsernameSuccess() throws Exception {
        UsernameLogin usernameLogin = new UsernameLogin("testUser", "testPass");
        when(loginService.loginByUsername(any(UsernameLogin.class))).thenReturn("mockToken");

        mockMvc.perform(post("/login/username")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usernameLogin)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Username login successful"))
                .andExpect(jsonPath("$.data").value("mockToken"));
    }

    @Test
    void loginByEmailSuccess() throws Exception {
        EmailLogin emailLogin = new EmailLogin("test@example.com", "123456");
        when(loginService.loginByEmail(any(EmailLogin.class))).thenReturn("mockToken");

        mockMvc.perform(post("/login/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emailLogin)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Email login successful"))
                .andExpect(jsonPath("$.data").value("mockToken"));
    }

    @Test
    void loginByIncorrectUsername() throws Exception {
        UsernameLogin usernameLogin = new UsernameLogin("testUser", "testPass");

        when(loginService.loginByUsername(any(UsernameLogin.class))).thenThrow(new CustomExceptions.LoginException("Incorrect username or password"));

        mockMvc.perform(post("/login/username")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usernameLogin)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Incorrect username or password"));
    }

    @Test
    void loginByUsernameInvalidData() throws Exception {
        UsernameLogin usernameLogin = new UsernameLogin("", "testPass");

        mockMvc.perform(post("/login/username")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usernameLogin)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message").value("Method or Argument not valid"));
    }

    @Test
    void loginByEmailInvalidData() throws Exception {
        EmailLogin emailLogin = new EmailLogin("invalidEmail", "123456");

        mockMvc.perform(post("/login/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emailLogin)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message").value("Method or Argument not valid"));
    }


    @Test
    void loginByEmailIncorrectVerificationCode() throws Exception {
        EmailLogin emailLogin = new EmailLogin("test@example.com", "wrongCode");

        when(loginService.loginByEmail(any(EmailLogin.class))).thenThrow(new CustomExceptions.LoginException("Incorrect Verification code"));

        mockMvc.perform(post("/login/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emailLogin)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Incorrect Verification code"));
    }
}
