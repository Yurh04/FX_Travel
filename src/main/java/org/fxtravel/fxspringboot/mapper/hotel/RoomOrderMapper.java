package org.fxtravel.fxspringboot.mapper.hotel;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.fxtravel.fxspringboot.common.E_PaymentStatus;
import org.fxtravel.fxspringboot.pojo.entities.RoomOrder;

import java.util.List;

public interface RoomOrderMapper extends BaseMapper<RoomOrder> {
    @Select("SELECT * FROM room_order WHERE user_id = #{userID}")
    List<RoomOrder> findByUserID(Integer userID);

    // 更新订单状态
    @Update("UPDATE room_order SET status = #{status} WHERE id = #{id}")
    int updateStatus(Integer id, E_PaymentStatus status);
}
