package org.fxtravel.fxspringboot.mapper.trainmeal;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.fxtravel.fxspringboot.pojo.entities.trainmeal.TrainMeal;

import java.util.List;

public interface TrainMealMapper extends BaseMapper<TrainMeal> {
    @Select("SELECT * FROM train_meal WHERE train_id = #{trainId} AND enabled = true ORDER BY price")
    List<TrainMeal> selectByTrain(Integer trainId);

    // 使用SELECT FOR UPDATE加锁查询
    @Select("SELECT * FROM train_meal WHERE id = #{id} FOR UPDATE")
    TrainMeal selectByIdForUpdate(int id);

    @Update("UPDATE train_meal SET " +
            "remain = remain - #{count} " +
            "WHERE id = #{id} AND remain >= #{count}")
    int deduct(int id, int count);

    @Update("UPDATE train_meal SET " +
            "remain = remain + #{count} " +
            "WHERE id = #{id}")
    void add(int id, int count);
}
