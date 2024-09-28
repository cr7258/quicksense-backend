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
import pro.quicksense.entity.User;
import pro.quicksense.config.TestSecurityConfig;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import pro.quicksense.service.UserService;

import java.util.Arrays;
import java.util.List;

@WebMvcTest(UserController.class)
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testRegister() throws Exception {
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("plainPassword");
        user.setEmail("test@163.com");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("testUser");
        savedUser.setPassword("encodedPassword");
        savedUser.setStatus(1);

        when(userService.register(any(User.class))).thenReturn(savedUser);

        mockMvc.perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("User registered successfully"))
                .andExpect(jsonPath("$.data.username").value("testUser"))
                .andExpect(jsonPath("$.data.id").value(1L));
    }

    @Test
    void testRegisterWithInvalidData() throws Exception {
        User invalidUser = new User();
        invalidUser.setUsername(""); // invalid username
        invalidUser.setPassword("plainPassword");
        invalidUser.setEmail("test@163.com");

        mockMvc.perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUser)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Method or Argument not valid"));
    }

    @Test
    void testUpdate() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("updatedUser");
        user.setPassword("newPlainPassword");
        user.setEmail("updated@163.com");

        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setUsername("updatedUser");
        updatedUser.setPassword("encodedNewPassword");
        updatedUser.setEmail("updated@163.com");
        updatedUser.setStatus(1);

        when(userService.update(any(User.class))).thenReturn(updatedUser);

        mockMvc.perform(post("/user/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("User updated successfully"))
                .andExpect(jsonPath("$.data.username").value("updatedUser"))
                .andExpect(jsonPath("$.data.id").value(1L));
    }

    @Test
    void testUpdateWithInvalidData() throws Exception {
        User invalidUser = new User();
        invalidUser.setId(1L);
        invalidUser.setUsername(""); // Invalid username
        invalidUser.setPassword("newPlainPassword");
        invalidUser.setEmail("updated@163.com");

        mockMvc.perform(post("/user/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUser)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Method or Argument not valid"));
    }

    @Test
    void testList() throws Exception {
        List<User> userList = Arrays.asList(
                new User(1L, "user1", "encodedPassword1", "user1@example.com", 1),
                new User(2L, "user2", "encodedPassword2", "user2@example.com", 1)
        );

        when(userService.findAll()).thenReturn(userList);

        mockMvc.perform(post("/user/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].username").value("user1"))
                .andExpect(jsonPath("$.data[0].email").value("user1@example.com"))
                .andExpect(jsonPath("$.data[1].id").value(2L))
                .andExpect(jsonPath("$.data[1].username").value("user2"))
                .andExpect(jsonPath("$.data[1].email").value("user2@example.com"));
    }
}
