package org.fxtravel.fxspringboot.pojo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "room")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;//房间唯一标识

    private Integer hotelId;

    private Integer typeId;//酒店内部使用的房型Id

    @Column(name = "name", nullable = false, length = 50)
    private String name;// 房型名称（如“⼤床房”）

    @Column(name = "remain", nullable = false)
    private Integer remain;// 剩余可售房量

    @Column(name = "price_per_night", nullable = false, precision = 10)
    private Double pricePerNight;// 每晚基础价格（元）
}
