package org.evlast.face_back.controller;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.evlast.face_back.domain.EquipmentBorrows;
import org.evlast.face_back.mapper.EquipmentBorrowsMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/equipment-borrows")
@Slf4j
@CrossOrigin
public class EquipmentBorrowsController {

    @Resource
    private EquipmentBorrowsMapper equipmentBorrowsMapper;

    @GetMapping("/page")
    public List<EquipmentBorrows> page() {
        return equipmentBorrowsMapper.selectList(null);
    }

    @DeleteMapping("/deleteById/{id}")
    public Integer deleteById(@PathVariable Integer id) {
        return equipmentBorrowsMapper.deleteById(id);
    }

    @PostMapping("/insert")
    public Integer insert(@RequestBody EquipmentBorrows equipmentBorrows) {
        return equipmentBorrowsMapper.insert(equipmentBorrows);
    }

    @PostMapping("/update")
    public Integer update(@RequestBody EquipmentBorrows equipmentBorrows) {
        return equipmentBorrowsMapper.updateById(equipmentBorrows);
    }
}
