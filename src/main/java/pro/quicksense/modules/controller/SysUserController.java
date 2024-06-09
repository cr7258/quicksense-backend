package pro.quicksense.modules.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.quicksense.modules.common.CommonConstant;
import pro.quicksense.modules.common.Result;
import pro.quicksense.modules.eneity.SysUser;
import pro.quicksense.modules.service.ISysUserService;
import pro.quicksense.modules.util.PasswordUtil;
import pro.quicksense.modules.util.oConvertUtils;

import java.util.Date;

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


    /**
     * 用户注册接口
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
        SysUser sysUser1 = sysUserService.getUserByName(username);
        if (ObjectUtil.isNotEmpty(sysUser1)) {
            result.setMessage("用户名已注册");
            result.setSuccess(false);
            return result;
        }

        SysUser sysUser2 = sysUserService.getUserByPhone(phone);
        if (sysUser2 != null) {
            result.setMessage("该手机号已注册");
            result.setSuccess(false);
            return result;
        }

        if (oConvertUtils.isNotEmpty(email)) {
            SysUser sysUser3 = sysUserService.getUserByEmail(email);
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
}
