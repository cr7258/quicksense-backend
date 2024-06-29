package pro.quicksense.modules.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import pro.quicksense.modules.entity.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
