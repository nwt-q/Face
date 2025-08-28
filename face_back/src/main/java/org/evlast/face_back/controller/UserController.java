package org.evlast.face_back.controller;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.evlast.face_back.domain.Users;
import org.evlast.face_back.domain.vo.UserFormVO;
import org.evlast.face_back.mapper.BookingsMapper;
import org.evlast.face_back.mapper.UsersMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
@CrossOrigin
public class UserController {

    @Resource
    private UsersMapper userMapper;
    @Resource
    private BookingsMapper bookingsMapper;

    @GetMapping("/page")
    public List<Users> page() {
        List<Users> users = userMapper.selectList(null);
        return users;
    }

//    @PostMapping("/page")
//    public Page<Users> page(@RequestBody UserQuery query, PageParam pageParam) {
//        LambdaQueryWrapper<Users> wrapper = new LambdaQueryWrapper<>();
//        wrapper.like(StringUtils.isNotBlank(query.getUsername()), Users::getUsername, query.getUsername())
//               .eq(StringUtils.isNotBlank(query.getStatus()), Users::getStatus, query.getStatus());
//
//        Page<Users> page = new Page<>(pageParam.getCurrentPage(), pageParam.getPageSize());
//        return userMapper.selectPage(page, wrapper);
//    }

    @DeleteMapping("/deleteById/{id}")
    public Integer deleteById(@PathVariable Integer id) {
        // 删除用户的同时需要将预约表中的数据删除
        final Users users = userMapper.selectById(id);
        users.setStatus("x");
        return userMapper.updateById(users);
    }

    @PostMapping("/insert")
    public Integer insert( @RequestBody UserFormVO form) {
        return userMapper.insert(form.toUser());
    }

    @PostMapping("/update")
    public Integer update(@RequestBody UserFormVO form) {
        return userMapper.updateById(form.toUser());
    }

}
