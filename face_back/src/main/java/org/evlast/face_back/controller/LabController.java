package org.evlast.face_back.controller;


import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.evlast.face_back.domain.Labs;
import org.evlast.face_back.domain.vo.EquipmentVO;
import org.evlast.face_back.domain.vo.LabsVO;
import org.evlast.face_back.domain.vo.UserBookVO;
import org.evlast.face_back.mapper.LabsMapper;
import org.evlast.face_back.service.LabsService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/labs")
@Slf4j
@CrossOrigin
public class LabController {

    @Resource
    private LabsMapper labsMapper;

    @RequestMapping("/")
    public String hello() {
        return "hello world";
    }

    /**
     * 获取所有实验室
     * @return
     */
    @GetMapping("/page")
    public List<Labs> page() {
        return labsMapper.getAll();
    }

    // 根据实验室名称查询
    @GetMapping("/getByName/{name}")
    public Labs getByName(@PathVariable String name) {
        return labsMapper.getByName(name);
    }


    /**
     * 根据id删除实验室
     * @param id
     * @return
     */
    @DeleteMapping("/deleteById/{id}")
    public Integer deleteById(@PathVariable Integer id) {
        return labsMapper.deleteById(id);
    }

    /**
     * 插入实验室
     * @param labs
     * @return
     */
    @PostMapping("/insert")
    public Integer insert(@RequestBody Labs labs) {
        return labsMapper.insert(labs);
    }

    /**
     * 更新实验室
     * @param labs
     * @return
     */
    @PostMapping("/update")
    public Integer update(@RequestBody Labs labs) {
        return labsMapper.updateById(labs);
    }


    @GetMapping("/getlabs")
    public List<LabsVO> getEquipment() {
        return labsMapper.selectLabs();
    }

    @GetMapping("/userget")
    public List<UserBookVO> userget() {
        final List<Labs> all = labsMapper.getAll();
        List<UserBookVO> userBookVOList = new ArrayList<>();
       for(Labs labs : all){
           UserBookVO userBookVO = new UserBookVO();
           userBookVO.setId(labs.getId());
           userBookVO.setName(labs.getName());
           userBookVO.setDescription(labs.getDescription());
           userBookVO.setAvailable(true);
           userBookVOList.add(userBookVO);
       }
       return userBookVOList;
    }
}
