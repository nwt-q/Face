package org.evlast.face_back.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.evlast.face_back.domain.Bookings;
import org.evlast.face_back.service.BookingsService;
import org.evlast.face_back.mapper.BookingsMapper;
import org.springframework.stereotype.Service;

/**
* @author NWT
* @description 针对表【bookings(实验室预约记录表)】的数据库操作Service实现
* @createDate 2025-04-23 22:54:39
*/
@Service
public class BookingsServiceImpl extends ServiceImpl<BookingsMapper, Bookings>
    implements BookingsService{

}




