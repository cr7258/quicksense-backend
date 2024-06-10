package pro.quicksense.modules.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import pro.quicksense.modules.common.Result;
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
     * 校验用户是否有效
     * @param sysUser
     * @return
     */
    Result<JSONObject> checkUserIsEffective(SysUser sysUser);



}