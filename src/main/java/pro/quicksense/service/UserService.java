package pro.quicksense.service;

import pro.quicksense.entity.User;
import java.util.List;


public interface UserService {
    User register(User user);

    User findByUsername(String username);

    User findByEmail(String email);

    List<User> findAll();

    User update(User user);
}