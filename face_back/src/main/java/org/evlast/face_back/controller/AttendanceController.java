package org.evlast.face_back.controller;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.evlast.face_back.domain.Attendance;
import org.evlast.face_back.mapper.AttendanceMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/attendance")
@Slf4j
@CrossOrigin
public class AttendanceController {

    @Resource
    private AttendanceMapper attendanceMapper;

    @GetMapping("/page")
    public List<Attendance> page() {
        return attendanceMapper.selectList(null);
    }

    @DeleteMapping("/deleteById/{id}")
    public Integer deleteById(@PathVariable Integer id) {
        return attendanceMapper.deleteById(id);
    }

    @PostMapping("/insert")
    public Integer insert(@RequestBody Attendance attendance) {
        return attendanceMapper.insert(attendance);
    }

    @PostMapping("/update")
    public Integer update(@RequestBody Attendance attendance) {
        return attendanceMapper.updateById(attendance);
    }
}
