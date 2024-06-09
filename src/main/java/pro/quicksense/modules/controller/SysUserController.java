package pro.quicksense.modules.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pro.quicksense.modules.common.CommonConstant;
import pro.quicksense.modules.common.Result;
import pro.quicksense.modules.eneity.SysLoginModel;
import pro.quicksense.modules.eneity.SysUser;
import pro.quicksense.modules.service.ISysUserService;
import pro.quicksense.modules.util.JwtUtil;
import pro.quicksense.modules.util.PasswordUtil;
import pro.quicksense.modules.util.RedisUtil;
import pro.quicksense.modules.util.oConvertUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.LinkedHashMap;

/**
 * <p>
 * 用户管理
 * </p>
 *
 * @Author lhn
 * @since 2024-06-09
 */

@Slf4j
@RestController
@RequestMapping("/sys/user")
public class SysUserController {

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 用户注册
     *
     * @param jsonObject
     * @return
     */
    @PostMapping("/register")
    public Result<JSONObject> userRegister(@RequestBody JSONObject jsonObject) {
        Result<JSONObject> result = new Result<JSONObject>();
        String phone = jsonObject.getString("phone");
        String username = jsonObject.getString("username");
        String email = jsonObject.getString("email");

        // 未设置用户名，则用手机号作为用户名
        if (oConvertUtils.isEmpty(username)) {
            username = phone;
        }

        // 未设置密码，则随机生成一个密码
        String password = jsonObject.getString("password");
        if (oConvertUtils.isEmpty(password)) {
            password = RandomUtil.randomString(8);
        }

        // 校验用户名、手机号、邮箱是否已注册
        SysUser sysUser1 = sysUserService.getOne(Wrappers.<SysUser>query().lambda().eq(SysUser::getUsername, username));
        if (ObjectUtil.isNotEmpty(sysUser1)) {
            result.setMessage("用户名已注册");
            result.setSuccess(false);
            return result;
        }

        SysUser sysUser2 = sysUserService.getOne(Wrappers.<SysUser>query().lambda().eq(SysUser::getPhone, phone));
        if (sysUser2 != null) {
            result.setMessage("该手机号已注册");
            result.setSuccess(false);
            return result;
        }

        if (oConvertUtils.isNotEmpty(email)) {
            SysUser sysUser3 = sysUserService.getOne(Wrappers.<SysUser>query().lambda().eq(SysUser::getEmail, email));
            if (sysUser3 != null) {
                result.setMessage("邮箱已被注册");
                result.setSuccess(false);
                return result;
            }
        }

        String realname = jsonObject.getString("realname");
        if (oConvertUtils.isEmpty(realname)) {
            realname = username;
        }

        try {
            SysUser user = new SysUser();
            user.setCreateTime(new Date());// 设置创建时间
            String salt = oConvertUtils.randomGen(8);
            String passwordEncode = PasswordUtil.encrypt(username, password, salt);
            user.setUsername(username);
            user.setRealName(realname);
            user.setPassword(passwordEncode);
            user.setSalt(salt);
            user.setEmail(email);
            user.setPhone(phone);
            user.setStatus(CommonConstant.USER_UNFREEZE);
            sysUserService.save(user);
            result.success("注册成功");
        } catch (Exception e) {
            result.error500("注册失败");
        }
        return result;
    }

    /**
     * 用户登录
     *
     * @return
     */
    @PostMapping("/login")
    public Result<JSONObject> login(@RequestBody SysLoginModel sysLoginModel) {
        Result<JSONObject> result = new Result<>();
        String username = sysLoginModel.getUsername();
        String password = sysLoginModel.getPassword();
        if(isLoginFailOvertimes(username)){
            return result.error500("该用户登录失败次数过多，请于10分钟后再次登录！");
        }

        // 校验用户是否存在且有效
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getUsername,username);
        SysUser sysUser = sysUserService.getOne(queryWrapper);
        result = sysUserService.checkUserIsEffective(sysUser);
        if(!result.isSuccess()) {
            return result;
        }

        // 校验用户名或密码是否正确
        String userpassword = PasswordUtil.encrypt(username, password, sysUser.getSalt());
        String syspassword = sysUser.getPassword();
        if (!syspassword.equals(userpassword)) {
            addLoginFailOvertimes(username);
            result.error500("用户名或密码错误");
            return result;
        }

        // 登录成功获取用户信息
        userInfo(sysUser, result);

        return result;
    }

    /**
     * 用户信息编辑
     */
    @PostMapping("/edit")
    public Result<Object> edit(@RequestBody SysUser sysUser) {
        Result<Object> result = new Result<>();
        try {
            sysUserService.updateById(sysUser);
            result.success("修改成功");
        } catch (Exception e) {
            result.error500("修改失败");
        }
        return result;
    }

    /**
     * 用户信息回显
     */
    @GetMapping("/info")
    public Result<?> info(HttpServletRequest request) {
        Result<JSONObject> result = new Result<>();
        String token = request.getHeader(CommonConstant.X_ACCESS_TOKEN);
        if(oConvertUtils.isEmpty(token)) {
            result.error500("token格式不对。或者已过期！");
            return result;
        }
        String username = JwtUtil.getUsername(token);
        SysUser sysUser = sysUserService.getOne(Wrappers.<SysUser>query().lambda().eq(SysUser::getUsername, username));

        if(sysUser==null) {
            result.error500("用户不存在！");
        }

        return Result.ok(sysUser);
    }


    /**
     * 退出登录
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public Result<Object> logout(HttpServletRequest request) {
        //用户退出逻辑
        String token = request.getHeader(CommonConstant.X_ACCESS_TOKEN);
        if(oConvertUtils.isEmpty(token)) {
            return Result.error("退出登录失败！");
        }
        String username = JwtUtil.getUsername(token);
        SysUser sysUser = sysUserService.getOne(Wrappers.<SysUser>query().lambda().eq(SysUser::getUsername, username));
        if(sysUser!=null) {
            //清空用户登录Token缓存
            redisUtil.del(CommonConstant.PREFIX_USER_TOKEN + token);
            return Result.ok("退出登录成功！");
        }else {
            return Result.error("Token无效!");
        }
    }

    /**
     * 用户信息
     *
     * @param sysUser
     * @param result
     * @return
     */
    private Result<JSONObject> userInfo(SysUser sysUser, Result<JSONObject> result) {
        String username = sysUser.getUsername();
        String syspassword = sysUser.getPassword();

        JSONObject obj = new JSONObject(new LinkedHashMap<>());

        //1.生成token
        String token = JwtUtil.sign(username, syspassword);
        // 设置token缓存有效时间
        redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, token);
        redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, JwtUtil.EXPIRE_TIME * 2 / 1000);
        obj.put("token", token);

        //设置登录用户信息
        obj.put("userInfo", sysUser);

        result.setResult(obj);
        result.success("登录成功");
        return result;
    }

    /**
     * 记录登录失败次数
     * @param username
     */
    private void addLoginFailOvertimes(String username){
        String key = CommonConstant.LOGIN_FAIL + username;
        Object failTime = redisUtil.get(key);
        Integer val = 0;
        if(failTime!=null){
            val = Integer.parseInt(failTime.toString());
        }
        // 10分钟，一分钟为60s
        redisUtil.set(key, ++val, 600);
    }

    /**
     * 登录失败超出次数5 返回true
     * @param username
     * @return
     */
    private boolean isLoginFailOvertimes(String username){
        String key = CommonConstant.LOGIN_FAIL + username;
        Object failTime = redisUtil.get(key);
        if(failTime!=null){
            Integer val = Integer.parseInt(failTime.toString());
            if(val>5){
                return true;
            }
        }
        return false;
    }
}
