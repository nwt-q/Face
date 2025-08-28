package org.evlast.face_back.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.evlast.face_back.domain.Users;
import org.evlast.face_back.service.UsersService;
import org.evlast.face_back.mapper.UsersMapper;
import org.springframework.stereotype.Service;

/**
* @author NWT
* @description 针对表【users(系统用户信息表)】的数据库操作Service实现
* @createDate 2025-04-23 22:54:39
*/
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users>
    implements UsersService{

}




