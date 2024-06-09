package pro.quicksense.modules.service;

import com.baomidou.mybatisplus.extension.service.IService;
import pro.quicksense.modules.eneity.SysUser;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @Author scott
 * @since 2018-12-20
 */
public interface ISysUserService extends IService<SysUser> {

    /**
     * 根据用户名查询
     * @param username 用户名
     * @return SysUser
     */
    SysUser getUserByName(String username);

    /**
     * 根据手机号获取用户名和密码
     * @param phone 手机号
     * @return SysUser
     */
     SysUser getUserByPhone(String phone);

    /**
     * 根据邮箱获取用户
     * @param email 邮箱
     * @return SysUser
     */
    SysUser getUserByEmail(String email);



}