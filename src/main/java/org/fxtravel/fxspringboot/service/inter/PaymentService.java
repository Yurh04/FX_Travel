package org.fxtravel.fxspringboot.service.inter;

import org.fxtravel.fxspringboot.common.E_PaymentStatus;
import org.fxtravel.fxspringboot.common.E_PaymentType;
import org.fxtravel.fxspringboot.pojo.dto.payment.PaymentQueryDTO;
import org.fxtravel.fxspringboot.pojo.dto.payment.PaymentResultDTO;
import org.fxtravel.fxspringboot.pojo.entities.payment;

import java.util.List;
import java.util.function.Supplier;

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
    payment createPayment(Integer userId, E_PaymentType type, Double amount,
                          Integer relatedId, Integer quantity, Integer goodId);

    /**
     * 完成支付
     * @param orderNumber 订单号
     * @return 是否成功完成支付
     */
    boolean completePayment(String orderNumber, Object data);

    /**
     * 支付失败
     * @param orderNumber 订单号
     * @return 是否成功标记为失败
     */
    boolean failPayment(String orderNumber, Object data);

    /**
     * 退款
     * @param orderNumber 订单号
     * @return 是否成功退款
     */
    boolean refundPayment(String orderNumber, Object data);

    /**
     * 完成交易（标记为不可退款状态）
     * @param orderNumber 订单号
     * @return 是否成功标记为完成
     */
    boolean finishPayment(String orderNumber, Object data);

    // -------------------- 支付模拟接口 --------------------
    /**
     * 模拟支付流程
     * @param orderNumber 订单号
     * @param timeout 超时时间(秒)
     * @return CompletableFuture<Boolean> 支付结果
     */
    PaymentResultDTO simulatePaymentProcess(String orderNumber, long timeout,
                                            Supplier<Boolean> inventoryDeduction, Supplier<Object> data);

    /**
     * 检查支付状态
     * @param orderNumber 订单号
     * @return 当前支付状态
     */
    PaymentResultDTO checkPaymentStatus(String orderNumber);

    /**
     * 检查支付状态
     * @param paymentId 订单Id
     * @return 当前支付状态
     */
    PaymentResultDTO checkPaymentStatus(Integer paymentId);
}