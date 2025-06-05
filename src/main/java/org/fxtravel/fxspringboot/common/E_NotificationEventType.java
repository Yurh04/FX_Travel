package org.fxtravel.fxspringboot.common;

public enum E_NotificationEventType {
    PAYMENT_CREATED,      // 支付订单创建
    PAYMENT_COMPLETED,    // 支付完成
    PAYMENT_FAILED,       // 支付失败
    PAYMENT_REFUNDED,     // 支付退款
    PAYMENT_FINISHED,     // 支付完成(不可退款)
    PAYMENT_TIMEOUT,      // 支付超时

    TICKET_PURCHASED,   //订票成功
    ORDER_CANCELLED,    //订单取消
    MEAL_ORDERED,       //订饭成功
    HOTEL_BOOKED        //定酒店成功
}
