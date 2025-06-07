// TrainSeatMapper.java
package org.fxtravel.fxspringboot.mapper.TrainSeat;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.fxtravel.fxspringboot.pojo.entities.TrainSeat;
import org.apache.ibatis.annotations.*;
import org.fxtravel.fxspringboot.common.SeatType;

import java.util.List;

@Mapper
public interface TrainSeatMapper extends BaseMapper<TrainSeat> {
    @Select("SELECT EXISTS(SELECT 1 FROM train_seat WHERE train_id = #{trainID} AND seat_type = #{seatType})")
    boolean existsByTrainAndType(@Param("trainID") Integer trainID, @Param("seatType") SeatType seatType);

    @Select("SELECT * FROM train_seat WHERE train_id = #{trainID}")
    List<TrainSeat> findByTrain(@Param("trainID") Integer trainID);

    @Update({
            "<script>",
            "UPDATE train_seat SET ",
            "remain = remain - 1, ",
            "seat_allocation = SET_BIT(seat_allocation, #{index}, 1)",
            "WHERE id = #{id} AND remain >= 1",
            "</script>"
    })
    int deduct(@Param("id") int id, @Param("index") int index);

    @Update({
            "<script>",
            "UPDATE train_seat SET ",
            "remain = remain + 1, ",
            "seat_allocation = SET_BIT(seat_allocation, #{index}, 0)",
            "WHERE id = #{id}",
            "</script>"
    })
    int add(@Param("id") int id, @Param("index") int index);
}