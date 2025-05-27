package org.fxtravel.fxspringboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.fxtravel.fxspringboot.common.E_PaymentStatus;
import org.fxtravel.fxspringboot.pojo.dto.trainmeal.TrainMealOrderQueryDTO;
import org.fxtravel.fxspringboot.pojo.entities.train_meal_order;

import java.time.LocalDateTime;
import java.util.List;

public interface TrainMealOrderMapper extends BaseMapper<train_meal_order> {
    // 基础查询 - 根据用户ID获取订单
    @Select("SELECT * FROM train_meal_order WHERE user_id = #{userId}")
    List<train_meal_order> selectByUserId(Integer userId);

    // 基础查询 - 根据车票预购ID获取订单
    @Select("SELECT * FROM train_meal_order WHERE ticket_reservation_id = #{ticketReservationId}")
    List<train_meal_order> selectByTicketReservationId(Integer ticketReservationId);

    // 全功能多条件查询
    @Select("<script>" +
            "SELECT * FROM train_meal_order WHERE 1=1" +
            "<if test='userId != null'> AND user_id = #{userId}</if>" +
            "<if test='ticketReservationId != null'> AND ticket_reservation_id = #{ticketReservationId}</if>" +
            "<if test='trainMealId != null'> AND train_meal_id = #{trainMealId}</if>" +
            "<if test='status != null'> AND status = #{status}</if>" +
            "<if test='createTimeStart != null'> AND create_time &gt;= #{createTimeStart}</if>" +
            "<if test='createTimeEnd != null'> AND create_time &lt;= #{createTimeEnd}</if>" +
            "</script>")
    List<train_meal_order> selectByConditions(
            Integer userId,
            Integer ticketReservationId,
            Integer trainMealId,
            E_PaymentStatus status,
            LocalDateTime createTimeStart,
            LocalDateTime createTimeEnd);

    // DTO参数版本的多条件查询
    @Select("<script>" +
            "SELECT * FROM train_meal_order WHERE 1=1" +
            "<if test='userId != null'> AND user_id = #{userId}</if>" +
            "<if test='ticketReservationId != null'> AND ticket_reservation_id = #{ticketReservationId}</if>" +
            "<if test='trainMealId != null'> AND train_meal_id = #{trainMealId}</if>" +
            "<if test='status != null'> AND status = #{status}</if>" +
            "<if test='createTimeStart != null'> AND create_time &gt;= #{createTimeStart}</if>" +
            "<if test='createTimeEnd != null'> AND create_time &lt;= #{createTimeEnd}</if>" +
            "</script>")
    List<train_meal_order> selectByConditionsDTO(TrainMealOrderQueryDTO queryDTO);

    // 更新订单状态
    @Update("UPDATE train_meal_order SET status = #{status} WHERE id = #{id}")
    int updateStatus(Integer id, E_PaymentStatus status);

    // 统计用户订单总金额
    @Select("SELECT COALESCE(SUM(total_amount), 0) FROM train_meal_order WHERE user_id = #{userId}")
    Double sumAmountByUserId(Integer userId);

    // 统计特定餐食的总销量
    @Select("SELECT COALESCE(SUM(quantity), 0) FROM train_meal_order WHERE train_meal_id = #{trainMealId}")
    Integer sumQuantityByTrainMealId(Integer trainMealId);
}