package org.fxtravel.fxspringboot.pojo.entities;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class HotelBooking {
    private String bookingId;// 订单唯⼀标识
    private String userId;// 下单⽤户 ID
    private String hotelId;// 关联酒店 ID
    private String roomTypeId;// 关联房型 ID
    private LocalDate checkInDate;// ⼊住⽇期（YYYY-MM-DD）
    private LocalDate checkOutDate;// 离店⽇期（YYYY-MM-DD）
    private String status; // 订单状态CONFIRMED,CANCELED,FAILED,PENDING
    private Double totalAmount;// 订单总⾦额（元）
    private LocalDateTime createdAt;// 创建时间（ISO 8601 格式）
    private Integer paymentId; // 关联支付系统
}
