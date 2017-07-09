package com.yyn.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
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
    @Select("select * from ${tableName} where type = #{type}")
    List<Map<String,String>> getDeviceByTable(@Param("tableName") String tableName,@Param("type")  String type);

    @Select("select * from ${tableName} where id = #{id}")
    List<Map<String,String>> getDeviceByTableId(@Param("tableName") String tableName,@Param("id") int id);

    @Select("select max(id) from ${_parameter}")
    Integer searchLastId(String tableName);

    @Delete("delete from ${tableName} where id = #{id}")
    Integer delete(@Param("id") String id,@Param("tableName") String tableName);
}
