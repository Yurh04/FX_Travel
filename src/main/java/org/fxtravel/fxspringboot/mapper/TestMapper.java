package org.fxtravel.fxspringboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.fxtravel.fxspringboot.pojo.entities.test_obj;

import java.util.List;

public interface TestMapper extends BaseMapper<test_obj> {
    @Select("select * from test_obj where string_value like concat('%', #{str}, '%')")
    List<test_obj> selectByStr(String str);
}
