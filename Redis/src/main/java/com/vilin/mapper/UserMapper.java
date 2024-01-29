package com.vilin.mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vilin.entity.User;

/**
 * @author luowei
 * @description 针对表【t_user】的数据库操作Mapper
 * @createDate 2024-01-15 18:19:06
 * @Entity com.vilin.entity.User
 */
public interface UserMapper extends BaseMapper<User> {

  int insertSelective(User user);

  int deleteByIdAndUsername(@Param("id") Long id, @Param("username") String username);

  int updateUsernameById(@Param("username") String username, @Param("id") Long id);
}




