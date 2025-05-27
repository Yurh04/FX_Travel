package org.fxtravel.fxspringboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.fxtravel.fxspringboot.common.E_PaymentStatus;
import org.fxtravel.fxspringboot.common.E_PaymentType;
import org.fxtravel.fxspringboot.pojo.dto.payment.PaymentQueryDTO;
import org.fxtravel.fxspringboot.pojo.entities.payment;

import java.time.LocalDateTime;
import java.util.List;

// 自动生成的表中，字段名会被应用数据库的命名风格
// 该系统为底层系统，不对用户开放，可以对管理员和其它模块开放
public interface PaymentMapper extends BaseMapper<payment> {
    // 基础查询 - 根据交易类型获取支付记录
    @Select("SELECT * FROM payment WHERE type = #{type}")
    List<payment> selectByType(E_PaymentType type);

    // 全功能多条件查询（type必须，其他条件可选）
    @Select("<script>" +
            "SELECT * FROM payment WHERE type = #{type}" +
            "<if test='orderNumber != null and orderNumber != \"\"'> AND order_number = #{orderNumber}</if>" +
            "<if test='userId != null'> AND user_id = #{userId}</if>" +
            "<if test='status != null'> AND status = #{status}</if>" +
            "<if test='amountMin != null'> AND amount &gt;= #{amountMin}</if>" +
            "<if test='amountMax != null'> AND amount &lt;= #{amountMax}</if>" +
            "<if test='timeMin != null'> AND payment_time &gt;= #{timeMin}</if>" +
            "<if test='timeMax != null'> AND payment_time &lt;= #{timeMax}</if>" +
            "<if test='relatedId != null and relatedId != \"\"'> AND related_id = #{relatedId}</if>" +
            "</script>")
    List<payment> selectByConditions(
            E_PaymentType type,
            String orderNumber,
            Integer userId,
            E_PaymentStatus status,
            Double amountMin,
            Double amountMax,
            LocalDateTime timeMin,
            LocalDateTime timeMax,
            Integer relatedId);

    // DTO参数版本的多条件查询
    @Select("<script>" +
            "SELECT * FROM payment WHERE type = #{type}" +
            "<if test='orderNumber != null and orderNumber != \"\"'> AND order_number = #{orderNumber}</if>" +
            "<if test='userId != null'> AND user_id = #{userId}</if>" +
            "<if test='status != null'> AND status = #{status}</if>" +
            "<if test='amountMin != null'> AND amount &gt;= #{amountMin}</if>" +
            "<if test='amountMax != null'> AND amount &lt;= #{amountMax}</if>" +
            "<if test='timeMin != null'> AND payment_time &gt;= #{timeMin}</if>" +
            "<if test='timeMax != null'> AND payment_time &lt;= #{timeMax}</if>" +
            "<if test='relatedId != null and relatedId != \"\"'> AND related_id = #{relatedId}</if>" +
            "</script>")
    List<payment> selectByConditionsDTO(PaymentQueryDTO queryDTO);

    // 根据订单号精确查询
    @Select("SELECT * FROM payment WHERE order_number = #{orderNumber}")
    payment selectByOrderNumber(String orderNumber);

    // 更新支付状态
    @Update("UPDATE payment SET status = #{status}, payment_time = #{paymentTime} WHERE order_number = #{orderNumber}")
    int updateStatus(String orderNumber, E_PaymentStatus status, LocalDateTime paymentTime);

    // 统计某类型的支付总金额
    @Select("SELECT COALESCE(SUM(amount), 0) FROM payment WHERE type = #{type} AND status = 'COMPLETED'")
    Double sumAmountByType(E_PaymentType type);
}
