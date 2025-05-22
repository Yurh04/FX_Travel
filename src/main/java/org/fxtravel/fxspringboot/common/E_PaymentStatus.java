package org.fxtravel.fxspringboot.common;

public enum E_PaymentStatus {
    IDLE,       // 未开启
    PENDING,    // 待支付
    COMPLETED,  // 已完成
    FAILED,     // 已失败
    REFUNDED    // 已退款
}
