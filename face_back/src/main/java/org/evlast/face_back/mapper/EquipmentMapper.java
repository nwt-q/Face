package org.evlast.face_back.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Select;
import org.evlast.face_back.domain.Equipment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.evlast.face_back.domain.vo.EquipmentVO;

import java.util.List;

/**
* @author NWT
* @description 针对表【equipment(实验设备详细信息表)】的数据库操作Mapper
* @createDate 2025-04-23 22:54:39
* @Entity org.evlast.face_back.domain.Equipment
*/
public interface EquipmentMapper extends BaseMapper<Equipment> {

    @Select("SELECT id, name FROM equipment")
    List<EquipmentVO> selectEquipment();

    @Select("SELECT * FROM equipment")
    List<Equipment> selectPage();
}




