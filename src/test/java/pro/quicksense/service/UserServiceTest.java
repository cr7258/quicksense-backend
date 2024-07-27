package pro.quicksense.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pro.quicksense.common.CommonConstant;
import pro.quicksense.entity.User;
import pro.quicksense.repository.UserRepository;
import pro.quicksense.service.impl.UserServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister() {
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("test123");

        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        User registeredUser = userService.register(user);

        assertEquals("testUser", registeredUser.getUsername());
        assertTrue(new BCryptPasswordEncoder().matches("test123", registeredUser.getPassword()));
        assertEquals(CommonConstant.USER_STATUS_NORMAL, registeredUser.getStatus());

        verify(userRepository, times(1)).save(any(User.class));
    }
}