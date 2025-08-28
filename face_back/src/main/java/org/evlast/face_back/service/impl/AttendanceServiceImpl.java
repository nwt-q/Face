package org.evlast.face_back.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.evlast.face_back.domain.Attendance;
import org.evlast.face_back.service.AttendanceService;
import org.evlast.face_back.mapper.AttendanceMapper;
import org.springframework.stereotype.Service;

/**
* @author NWT
* @description 针对表【attendance(实验室使用考勤记录表)】的数据库操作Service实现
* @createDate 2025-04-23 22:54:39
*/
@Service
public class AttendanceServiceImpl extends ServiceImpl<AttendanceMapper, Attendance>
    implements AttendanceService{

}




