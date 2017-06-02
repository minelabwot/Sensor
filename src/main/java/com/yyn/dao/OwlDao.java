package com.yyn.dao;

import com.yyn.model.MyOwl;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.JDBCType;
import java.util.List;

/**
 * Created by koala on 2017/5/15.
 */
@Component
public interface OwlDao {

    @Insert("insert into owldata (owl_name,owl_filename,owl_root,owl_description) values" +
            " (#{name},#{file},#{root},#{description})")
    void inserOwl(MyOwl owl);

    @Results({
            @Result(property = "id",column = "owl_id",javaType = Integer.class,jdbcType = JdbcType.INTEGER),
            @Result(property = "root",column = "owl_root",javaType = String.class,jdbcType = JdbcType.VARCHAR),
            @Result(property = "rule",column = "owl_rule",javaType = String.class,jdbcType = JdbcType.VARCHAR),
            @Result(property="name",column="owl_name",javaType=String.class,jdbcType=JdbcType.VARCHAR),
            @Result(property="file",column="owl_filename",javaType=String.class,jdbcType=JdbcType.VARCHAR),
            @Result(property="description",column="owl_description",javaType=String.class,jdbcType=JdbcType.VARCHAR)
    })
    @Select("select owl_id,owl_root,owl_rule,owl_name,owl_filename,owl_description from owldata")
    List<MyOwl> queryAll();

    @Delete("delete from owldata where owl_root = #{file}")
    void deleteByFile(String file);

    @Delete("delete from owldata where owl_id = #{id}")
    void deleteById(int id);

    @Update("update owldata set owl_rule = #{owlRule} where owl_file=#{owlFile}")
    void saveRule(@Param("owlFile") String owlFile,@Param("owlRule") String owlRule);

    @Select("select owl_rule from owldata where owl_id=#{id}")
    String searchRuleById(int id);
}
