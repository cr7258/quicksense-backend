package pro.quicksense.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.quicksense.common.ApiResponseBuilder;
import pro.quicksense.entity.login.EmailLogin;
import pro.quicksense.entity.login.UsernameLogin;
import pro.quicksense.service.LoginService;

@Tag(name="Login", description="Login API")
@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/username")
    public ResponseEntity<Object> loginByUsername(@RequestBody @Valid UsernameLogin usernameLogin) {
        return ApiResponseBuilder.success(HttpStatus.OK, "Username login successful", loginService.loginByUsername(usernameLogin));
    }

    @PostMapping("/email")
    public ResponseEntity<Object> loginByEmail(@RequestBody @Valid EmailLogin emailLogin) {
        return ApiResponseBuilder.success(HttpStatus.OK, "Email login successful", loginService.loginByEmail(emailLogin));
    }
}