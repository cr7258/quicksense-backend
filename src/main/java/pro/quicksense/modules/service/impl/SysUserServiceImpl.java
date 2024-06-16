package pro.quicksense.modules.service.impl;


import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.quicksense.modules.common.CommonConstant;
import pro.quicksense.modules.common.Result;
import pro.quicksense.modules.eneity.User;
import pro.quicksense.modules.mapper.SysUserMapper;
import pro.quicksense.modules.service.ISysUserService;


@Service
@Slf4j
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, User> implements ISysUserService {
    @Override
    public Result<String> checkUserIsEffective(User user) {
        Result<String> result = new Result<>();

        //情况1：根据用户信息查询，该用户不存在
        if (ObjectUtil.isEmpty(user)) {
            result.setCode(500);
            result.setMsg("该用户不存在，请注册");
            return result;
        }

        //情况2：根据用户信息查询，该用户已冻结
        if (CommonConstant.USER_STATUS_NORMAL.equals(user.getStatus())) {
            result.setCode(500);
            result.setMsg("该用户已冻结");
            return result;
        }

        result.setCode(200);
        return result;
    }
}