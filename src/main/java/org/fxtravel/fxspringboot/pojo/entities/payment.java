package org.fxtravel.fxspringboot.pojo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fxtravel.fxspringboot.common.E_PaymentStatus;
import org.fxtravel.fxspringboot.common.E_PaymentType;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String orderNumber; // 订单号
    private Integer userId; // 用户ID
    @Enumerated(EnumType.STRING)
    private E_PaymentType type; // 交易类型
    @Enumerated(EnumType.STRING)
    private E_PaymentStatus status; // 支付状态
    private Double amount; // 支付金额
    private LocalDateTime paymentTime; // 支付时间
    private Integer relatedId; // 关联的业务ID（如车票ID、餐食订单ID、酒店订单ID）
}
