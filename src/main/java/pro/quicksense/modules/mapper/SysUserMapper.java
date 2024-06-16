package pro.quicksense.modules.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import pro.quicksense.modules.eneity.User;

@Mapper
public interface SysUserMapper extends BaseMapper<User> {
}
