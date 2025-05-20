package org.fxtravel.fxspringboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.fxtravel.fxspringboot.pojo.entities.train_meal;

import java.util.List;

public interface TrainMealMapper extends BaseMapper<train_meal> {
    // 基础查询
    @Select("SELECT * FROM train_meal WHERE train_id = #{trainId}")
    List<train_meal> selectByTrain4Admin(Integer trainId);

    @Select("SELECT * FROM train_meal WHERE train_id = #{trainId} AND enabled = true")
    List<train_meal> selectByTrain4User(Integer trainId);

    // 全功能多条件查询（trainId必须，其他条件可选）
    @Select("<script>" +
            "SELECT * FROM train_meal WHERE train_id = #{trainId}" +
            "<if test='name != null and name != \"\"'> AND name LIKE CONCAT('%', #{name}, '%')</if>" +
            "<if test='mealTime != null'> AND mealTime = #{mealTime}</if>" +
            "<if test='priceMin != null'> AND price &gt;= #{priceMin}</if>" +
            "<if test='priceMax != null'> AND price &lt;= #{priceMax}</if>" +
            "<if test='remain == true'> AND remain &gt; 0</if>" +
            "<if test='enabled != null'> AND enabled = #{enabled}</if>" +
            "</script>")
    List<train_meal> selectByConditions4Admin(
            Integer trainId,
            String name,
            String mealTime,
            Double priceMin,
            Double priceMax,
            Boolean remain,
            Boolean enabled);

    // 用户版本多条件查询（自动过滤enabled=false）
    @Select("<script>" +
            "SELECT * FROM train_meal WHERE train_id = #{trainId} AND enabled = true" +
            "<if test='name != null and name != \"\"'> AND name LIKE CONCAT('%', #{name}, '%')</if>" +
            "<if test='mealTime != null'> AND mealTime = #{mealTime}</if>" +
            "<if test='priceMin != null'> AND price &gt;= #{priceMin}</if>" +
            "<if test='priceMax != null'> AND price &lt;= #{priceMax}</if>" +
            "<if test='remain == true'> AND remain &gt; 0</if>" +
            "</script>")
    List<train_meal> selectByConditions4User(
            Integer trainId,
            String name,
            String mealTime,
            Double priceMin,
            Double priceMax,
            Boolean remain);
}