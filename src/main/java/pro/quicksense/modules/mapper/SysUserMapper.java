package pro.quicksense.modules.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import pro.quicksense.modules.eneity.SysUser;

@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
    SysUser getUserByName(@Param("username") String username);

    SysUser getUserByPhone(@Param("phone") String phone);

    SysUser getUserByEmail(@Param("email")String email);
}
