package com.yyn.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by koala on 2017/5/19.
 */
@Component
public interface BaseDeviceDao {
    @Select("select * from ${_parameter}")
    List<Map<String,String>> getDeviceByTable(String tableName);

    @Select("select max(id) from ${_parameter}")
    Integer searchLastId(String tableName);
}
