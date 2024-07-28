package pro.quicksense.service;

import pro.quicksense.entity.login.EmailLogin;
import pro.quicksense.entity.login.UsernameLogin;

public interface LoginService {
    String loginByUsername(UsernameLogin usernameLogin);
    String loginByEmail(EmailLogin emailLogin);
}
