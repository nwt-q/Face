package org.evlast.face_back.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.evlast.face_back.domain.Labs;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.evlast.face_back.domain.vo.EquipmentVO;
import org.evlast.face_back.domain.vo.LabsVO;

import java.util.List;

/**
* @author NWT
* @description 针对表【labs(实验室基本信息表)】的数据库操作Mapper
* @createDate 2025-04-23 22:54:39
* @Entity org.evlast.face_back.domain.Labs
*/
@Mapper
public interface LabsMapper extends BaseMapper<Labs> {

    @Select("SELECT * FROM labs")
    List<Labs> getAll();

    @Select("SELECT * FROM labs WHERE name = #{name}")
    Labs getByName(String name);

    @Select("SELECT id, name FROM labs")
    List<LabsVO> selectLabs();

    @Select("SELECT id, name FROM labs WHERE name = #{labName}")
    Labs selectByName(String labName);
}




