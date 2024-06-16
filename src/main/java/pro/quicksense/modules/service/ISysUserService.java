package pro.quicksense.modules.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import pro.quicksense.modules.common.Result;
import pro.quicksense.modules.eneity.User;


public interface ISysUserService extends IService<User> {
    Result<?> checkUserIsEffective(User user);
}