package com.vilin.bitmap.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vilin.entity.User;
import com.vilin.bitmap.service.UserService;
import com.vilin.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
 * @author luowei
 * @description 针对表【t_user】的数据库操作Service实现
 * @createDate 2024-01-15 18:19:06
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

}




