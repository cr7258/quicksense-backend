package pro.quicksense.modules.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pro.quicksense.modules.common.CommonConstant;
import pro.quicksense.modules.common.Result;
import pro.quicksense.modules.eneity.User;
import pro.quicksense.modules.service.ISysUserService;

import java.util.Date;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/user")
public class SysUserController {

    @Autowired
    private ISysUserService sysUserService;


    @PostMapping("/register")
    public Result<?> userRegister(@RequestBody JSONObject jsonObject) {
        Result<?> result = new Result<>();
        String username = jsonObject.getString("username");
        String email = jsonObject.getString("email");
        String password = jsonObject.getString("password");
        String confirmPassword = jsonObject.getString("confirmPassword");
        String realName = jsonObject.getString("realName");

        // Validate email address format
        String emailPattern = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
        if (!email.matches(emailPattern)) {
            result.setCode(400);
            result.setMsg("Invalid email format");
            return result;
        }

        // Validate password format
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,20}$";
        if (!password.matches(passwordPattern)) {
            result.setCode(400);
            result.setMsg("Password must contain letters, numbers, and special characters");
            return result;
        }

        // Check if password and confirm password are equal
        if (!password.equals(confirmPassword)) {
            result.setCode(400);
            result.setMsg("Password and confirm password do not match");
            return result;
        }

        //TODO: Email verification code functionality to be implemented

        // Encrypt the password
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(password);

        try {
            User user = new User();
            user.setCreateTime(new Date());
            user.setUsername(username);
            user.setRealName(realName);
            user.setPassword(encodedPassword);
            user.setEmail(email);
            user.setStatus(CommonConstant.USER_STATUS_FROZEN);
            sysUserService.save(user);
            result.setCode(200);
            result.setMsg("User registered successfully");
        } catch (DataIntegrityViolationException e) {
            result.setCode(400);
            result.setMsg("Username or email already exists");
        } catch (Exception e) {
            result.setCode(500);
            result.setMsg("An error occurred during registration");
        }

        return result;
    }


    @PostMapping("/login")
    public Result<?> login(@RequestBody JSONObject jsonObject) {
        Result<?> result = new Result<>();
        String username = jsonObject.getString("username");
        String password = jsonObject.getString("password");

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        User user = sysUserService.getOne(queryWrapper);

        // Check if the user exists and is effective
        result = sysUserService.checkUserIsEffective(user);
        if (result.getCode() != 200) {
            return result;
        }

        // Check if the username or password is correct
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        boolean matches = passwordEncoder.matches(password, user.getPassword());
        if (!matches) {
            result.setCode(400);
            result.setMsg("Incorrect username or password");
            return result;
        }

        Result<String> loginResult = new Result<>();
        // Login successful, generate token and return
        userInfo(user, loginResult);
        return loginResult;
    }

    @PostMapping("/edit")
    public Result<Object> edit(@RequestBody User user) {
        Result<Object> result = new Result<>();
        try {
            sysUserService.updateById(user);
            result.setCode(200);
            result.setMsg("edit success");
        } catch (Exception e) {
            result.setCode(500);
            result.setMsg("edit fail");
        }
        return result;
    }

    @GetMapping("/userInfo")
    public Result<?> info(@RequestParam(value = "id", required = false) String id) {
        User user = new User();

        if (StringUtils.isNotBlank(id)) {
            user = sysUserService.getById(id);
        } else {
            List<User> userList = sysUserService.list();
            Result<List<User>> result = new Result<>();
            result.setCode(200);
            result.setMsg("User list");
            result.setData(userList);
        }

        Result<User> loginResult = new Result<>();
        loginResult.setCode(200);
        loginResult.setMsg("User info");
        loginResult.setData(user);
        return loginResult;
    }

    private Result<String> userInfo(User user, Result<String> result) {
        String token = generateToken(user);

        result.setCode(200);
        result.setMsg("Login success");
        result.setData(token);

        return result;
    }

    private String generateToken(User user) {
        // Set token expiration time, here set to 1 day
        Date expiration = new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000);

        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("userId", user.getId())
                .setIssuedAt(new Date())
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS512, "secret")  // todo For testing only, please use a different secret key in production
                .compact();
    }
}
