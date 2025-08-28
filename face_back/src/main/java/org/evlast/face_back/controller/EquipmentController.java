package org.evlast.face_back.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.evlast.face_back.domain.Equipment;
import org.evlast.face_back.domain.Labs;
import org.evlast.face_back.domain.Users;
import org.evlast.face_back.domain.vo.EquipmentPageVO;
import org.evlast.face_back.domain.vo.EquipmentVO;
import org.evlast.face_back.mapper.EquipmentMapper;
import org.evlast.face_back.mapper.LabsMapper;
import org.evlast.face_back.mapper.UsersMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/equipment")
@Slf4j
@CrossOrigin
public class EquipmentController {

    @Resource
    private EquipmentMapper equipmentMapper;

    @Resource
    @Lazy
    private LabsMapper labsMapper;

    @Resource
    @Lazy
    private UsersMapper usersMapper;

    @GetMapping("/page")
    public List<EquipmentPageVO> page() {
        final List<Equipment> equipmentIPage = equipmentMapper.selectPage();
        List<EquipmentPageVO> equipmentPageVOList = new ArrayList<>();

        for(Equipment equipment : equipmentIPage) {
            EquipmentPageVO equipmentPageVO = new EquipmentPageVO();
            equipmentPageVO.setId(equipment.getId());
            equipmentPageVO.setName(equipment.getName());
            equipmentPageVO.setCreatedAt(equipment.getCreatedAt());
            equipmentPageVO.setStatus(equipment.getStatus());
            equipmentPageVO.setBorrowCount(equipment.getBorrowCount());
            final Labs labs = labsMapper.selectById(equipment.getLabId());
            if(labs == null){ equipmentPageVO.setLabName(""); } else equipmentPageVO.setLabName(labs.getName());
            final Users users = usersMapper.selectById(equipment.getLastBorrower());
            if(users == null){equipmentPageVO.setLastBorrowerName("");} else equipmentPageVO.setLastBorrowerName(users.getRealName());
            equipmentPageVOList.add(equipmentPageVO);
        }

        return equipmentPageVOList;
    }

    @DeleteMapping("/deleteById/{id}")
    public Integer deleteById(@PathVariable Integer id) {
        return equipmentMapper.deleteById(id);
    }

    @PostMapping("/insert")
    public Integer insert(@RequestBody Equipment equipment) {
        return equipmentMapper.insert(equipment);
    }

    @PostMapping("/update")
    public Integer update(@RequestBody Equipment equipment) {
        return equipmentMapper.updateById(equipment);
    }
    // 获取实验室id 和name
    @GetMapping("/getequipment")
    public List<EquipmentVO> getEquipment() {
        return equipmentMapper.selectEquipment();
    }
}
