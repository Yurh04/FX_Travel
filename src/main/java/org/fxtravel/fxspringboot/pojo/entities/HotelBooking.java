package org.fxtravel.fxspringboot.pojo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fxtravel.fxspringboot.common.E_HotelBookingStatus;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class HotelBooking {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "booking_id", columnDefinition = "VARCHAR(36)", nullable = false)
    private String bookingId;// 订单唯⼀标识

    @Column(name = "user_id", nullable = false, length = 36)
    private int userId;// 下单⽤户 ID

    @Column(name = "hotel_id", nullable = false, length = 36)
    private String hotelId;// 关联酒店 ID

    @Column(name = "room_type_id", nullable = false, length = 36)
    private String roomTypeId;// 关联房型 ID

    @Column(name = "check_in_date", nullable = false)
    private LocalDate checkInDate;// ⼊住⽇期（YYYY-MM-DD）

    @Column(name = "check_out_date", nullable = false)
    private LocalDate checkOutDate;// 离店⽇期（YYYY-MM-DD）

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private E_HotelBookingStatus status;// 订单状态CONFIRMED,CANCELED,FAILED,PENDING

    @Column(name = "total_amount", nullable = false, precision = 10)
    private Double totalAmount;// 订单总⾦额（元）

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;// 创建时间（ISO 8601 格式）

    @Column(name = "payment_id")
    private Integer paymentId;// 关联支付系统

}
