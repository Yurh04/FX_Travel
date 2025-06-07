// TrainMapper.java
package org.fxtravel.fxspringboot.mapper.trainseat;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.fxtravel.fxspringboot.pojo.entities.Train;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface TrainMapper extends BaseMapper<Train> {
    @Select("SELECT * FROM train WHERE from_station = #{fromStation} AND to_station = #{toStation} " +
            "AND departure_time BETWEEN #{start} AND #{end} ORDER BY departure_time")
    List<Train> findByRouteAndTimeOrderByTime(String fromStation, String toStation,
                                              LocalDateTime start, LocalDateTime end);

    @Select("SELECT * FROM train WHERE from_station = #{fromStation} AND to_station = #{toStation} " +
            "AND departure_time BETWEEN #{start} AND #{end} ORDER BY duration_minutes")
    List<Train> findByRouteAndTimeOrderByDuration(String fromStation, String toStation,
                                                  LocalDateTime start, LocalDateTime end);
}