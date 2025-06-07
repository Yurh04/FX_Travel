package org.fxtravel.fxspringboot.mapper.hotel;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.fxtravel.fxspringboot.pojo.entities.Room;
import org.fxtravel.fxspringboot.pojo.entities.TrainSeat;

import java.util.List;

public interface RoomMapper extends BaseMapper<Room> {
    @Select("SELECT * FROM room WHERE hotel_id = #{hotelId} ORDER BY type_id")
    List<Room> findByHotel(@Param("hotelId") Integer hotelId);

    @Update("UPDATE room SET remain = remain - #{count} WHERE id = #{id} AND remain >= #{count}")
    int deduct(int id, int count);

    @Update("UPDATE room SET remain = remain + #{count} WHERE id = #{id}")
    void add(int id, int count);
}
