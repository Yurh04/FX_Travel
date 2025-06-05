package org.fxtravel.fxspringboot.enent.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.fxtravel.fxspringboot.common.E_PaymentStatus;

@Data
@AllArgsConstructor
public class PaymentInfo {
    private Integer orderId;                // 订单ID
    private Integer goodId;                 // 商品ID
    private Integer quantity;               // 购买数量
    private E_PaymentStatus newStatus;      // payment的状态
    private Integer userId;                 // 用户ID
}
