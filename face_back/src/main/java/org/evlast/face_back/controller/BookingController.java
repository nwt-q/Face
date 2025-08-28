package org.evlast.face_back.controller;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.evlast.face_back.domain.Bookings;
import org.evlast.face_back.domain.Equipment;
import org.evlast.face_back.domain.Labs;
import org.evlast.face_back.domain.Users;
import org.evlast.face_back.domain.vo.BookingVO;
import org.evlast.face_back.domain.vo.BookingsVO;
import org.evlast.face_back.mapper.BookingsMapper;
import org.evlast.face_back.mapper.EquipmentMapper;
import org.evlast.face_back.mapper.LabsMapper;
import org.evlast.face_back.mapper.UsersMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/bookings")
@Slf4j
@CrossOrigin
public class BookingController {

    @Resource
    private BookingsMapper bookingMapper;

    @Resource
    @Lazy
    private UsersMapper usersMapper;

    @Resource
    @Lazy
    private LabsMapper labsMapper;

    @Resource
    @Lazy
    private EquipmentMapper equipmentMapper;

    @GetMapping("/page")
    public List<BookingsVO> page() {
        final List<Bookings> bookings = bookingMapper.selectList(null);
        List<BookingsVO> bookingsVOList = new ArrayList<>();
        for (Bookings booking : bookings) {
            BookingsVO bookingsVO = new BookingsVO();
            bookingsVO.setId(booking.getId());
            bookingsVO.setTimeSlots(booking.getTimeSlots());
            bookingsVO.setArrivalDate(booking.getArrivalDate());
            bookingsVO.setStatus(booking.getStatus());
            bookingsVO.setReason(booking.getReason());
            bookingsVO.setCreatedAt(booking.getCreatedAt());
            bookingsVOList.add(bookingsVO);

            // 获取用户名
            final Users u = usersMapper.selectById(booking.getUserId());
            // 获取实验室
            final Labs labs = labsMapper.selectById(booking.getLabId());
            bookingsVO.setUserName(u.getRealName());
            bookingsVO.setLabName(labs.getName());

            bookingsVOList.add(bookingsVO);
        }
        return bookingsVOList;
    }

    @DeleteMapping("/deleteById/{id}")
    public Integer deleteById(@PathVariable Integer id) {
        return bookingMapper.deleteById(id);
    }

    @PostMapping("/insert")
    public Integer insert(@RequestBody Bookings booking) {
        return bookingMapper.insert(booking);
    }

    @PostMapping("/update")
    public Integer update(@RequestBody Bookings booking) {
        return bookingMapper.updateById(booking);
    }

    @PostMapping("/inserts")
    public Integer inserts(@RequestBody BookingVO bookings) {
        // 插入预约表
        Bookings booking = new Bookings();
        // 默认审核(即将到来)
        final Users users = usersMapper.selectById(bookings.getId());
        if(users != null) {
            booking.setUserId(users.getId());
        } else {
            booking.setUserId(1);
        }
        // 通过姓名获取id
        final Labs labs = labsMapper.selectByName(bookings.getLabName());

        if(labs != null) {booking.setLabId(labs.getId());} else {booking.setLabId(1);}

        booking.setReason(bookings.getReason());

        booking.setArrivalDate(bookings.getArrivalDate());

        booking.setStatus("pending");
        booking.setReason(bookings.getReason());
        booking.setCreatedAt(LocalDateTime.now());
        booking.setTimeSlots("17:46:43");
        // 设备状态更新
        for(Integer equipmentId : bookings.getSelectedEquipment()) {
            final Equipment equipment = equipmentMapper.selectById(equipmentId);
            if(equipment != null) {
                equipment.setStatus("borrowed");
                equipment.setLastBorrower(bookings.getId());
                equipment.setCreatedAt(LocalDateTime.now());
                equipmentMapper.updateById(equipment);
            }
        }
        return bookingMapper.insert(booking);
    }
}
