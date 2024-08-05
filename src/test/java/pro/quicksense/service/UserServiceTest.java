package pro.quicksense.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pro.quicksense.common.Constant;
import pro.quicksense.entity.User;
import pro.quicksense.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister() {
        String username = "testUser";
        String password = "test123";
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        User registeredUser = userService.register(user);

        assertEquals(username, registeredUser.getUsername());
        assertTrue(new BCryptPasswordEncoder().matches(password, registeredUser.getPassword()));
        assertEquals(Constant.USER_STATUS_NORMAL, registeredUser.getStatus());

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testFindByUsername() {
        String username = "testUser";
        User user = new User();
        user.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(user);
        User foundUser = userService.findByUsername(username);

        assertEquals(username, foundUser.getUsername());
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void testFindByEmail() {
        String email = "test@example.com";
        User user = new User();
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(user);
        User foundUser = userService.findByEmail(email);

        assertEquals(email, foundUser.getEmail());
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void testUpdate() {
        User user = new User();
        user.setUsername("updatedUser");
        user.setPassword("newPassword");

        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        User updatedUser = userService.update(user);

        assertEquals("updatedUser", updatedUser.getUsername());
        assertEquals("newPassword", updatedUser.getPassword());
        verify(userRepository, times(1)).save(any(User.class));
    }
}