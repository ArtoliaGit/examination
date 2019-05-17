package com.bsoft.examination.mapper.test;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface TestMapper {

    @Select("select id from system_user")
    List<String> test();

    @Select("select * from user")
    List<Map> getUser();
}
