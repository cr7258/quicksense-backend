package pro.quicksense.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.quicksense.common.ApiResponseBuilder;
import pro.quicksense.entity.User;
import pro.quicksense.service.UserService;


@Tag(name = "User", description = "User management APIs")
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody @Valid User user) {
        return ApiResponseBuilder.success(HttpStatus.CREATED, "User registered successfully", userService.register(user));
    }

    @PostMapping("/edit")
    public ResponseEntity<Object> update(@RequestBody @Valid User user) {
        return ApiResponseBuilder.success(HttpStatus.OK, "User updated successfully", userService.update(user));
    }

    @GetMapping("/list")
    public ResponseEntity<Object> list() {
        return ApiResponseBuilder.success(HttpStatus.OK, "User information listed successfully", userService.findAll());
    }
}
