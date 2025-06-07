// TrainSeatOrder.java (更新实体类)
package org.fxtravel.fxspringboot.pojo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fxtravel.fxspringboot.common.E_PaymentStatus;

import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "train_seat_order")
public class TrainSeatOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String orderNumber;

    @Column(name = "user_id")
    private Integer userId;

    private Integer trainId;

    @Column(name = "train_seat_id")
    private Integer trainSeatId;

    private String seatNumber;

    @Column(name = "related_payment_id")
    private Integer relatedPaymentId;

    @Column(name = "total_amount")
    private Double totalAmount;

    @Enumerated(EnumType.STRING)
    private E_PaymentStatus status;

    @Column(name = "create_time")
    private LocalDateTime createTime;
}