package org.fxtravel.fxspringboot.service.inter;

import org.fxtravel.fxspringboot.common.E_PaymentStatus;
import org.fxtravel.fxspringboot.common.E_PaymentType;
import org.fxtravel.fxspringboot.pojo.dto.PaymentDTO;
import org.fxtravel.fxspringboot.pojo.dto.PaymentQueryDTO;
import org.fxtravel.fxspringboot.pojo.dto.PaymentResultDTO;
import org.fxtravel.fxspringboot.pojo.entities.payment;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface PaymentService {
    // -------------------- 基础支付操作 --------------------
    /**
     * 创建支付订单
     * @param userId 用户ID
     * @param type 支付类型
     * @param amount 支付金额
     * @param relatedId 关联业务ID
     * @return 生成的支付订单
     */
    payment createPayment(Integer userId, E_PaymentType type, Double amount, Integer relatedId);

    /**
     * 完成支付
     * @param orderNumber 订单号
     * @return 是否成功完成支付
     */
    boolean completePayment(String orderNumber);

    /**
     * 支付失败
     * @param orderNumber 订单号
     * @return 是否成功标记为失败
     */
    boolean failPayment(String orderNumber);

    /**
     * 退款
     * @param orderNumber 订单号
     * @return 是否成功退款
     */
    boolean refundPayment(String orderNumber);

    // -------------------- 管理员查询接口 --------------------
    /**
     * 根据ID获取支付记录
     * @param id 支付记录ID
     * @return 支付记录
     */
    payment getPaymentById(Integer id);

    /**
     * 根据订单号获取支付记录
     * @param orderNumber 订单号
     * @return 支付记录
     */
    payment getPaymentByOrderNumber(String orderNumber);

    /**
     * 根据条件查询支付记录
     * @param queryDTO 查询条件
     * @return 符合条件的支付记录列表
     */
    List<payment> queryPayments(PaymentQueryDTO queryDTO);

    /**
     * 根据交易类型获取支付记录
     * @param type 交易类型
     * @return 支付记录列表
     */
    List<payment> getPaymentsByType(E_PaymentType type);

    /**
     * 统计某类型的支付总金额
     * @param type 交易类型
     * @return 总金额
     */
    Double sumAmountByType(E_PaymentType type);

    // -------------------- 支付模拟接口 --------------------
    /**
     * 模拟支付流程
     * @param orderNumber 订单号
     * @param timeout 超时时间(秒)
     * @return CompletableFuture<Boolean> 支付结果
     */
    PaymentResultDTO simulatePaymentProcess(String orderNumber, long timeout);

    /**
     * 检查支付状态
     * @param orderNumber 订单号
     * @return 当前支付状态
     */
    E_PaymentStatus checkPaymentStatus(String orderNumber);
}