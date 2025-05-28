package org.fxtravel.fxspringboot.pojo.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fxtravel.fxspringboot.common.SeatType;

import java.math.BigDecimal;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TrainSeat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // 车次关联（外键）
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "train_id", nullable = false)
    private Train train;

    // 座位类型（枚举）
    @Enumerated(EnumType.STRING)
    @Column(name = "seat_type", nullable = false)
    private SeatType seatType;

    // 票价
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    // 剩余票数
    @Column(nullable = false)
    private Integer available;
}
