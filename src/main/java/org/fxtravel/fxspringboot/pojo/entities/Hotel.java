package org.fxtravel.fxspringboot.pojo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Hotel {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "hotel_id", columnDefinition = "VARCHAR(36)", nullable = false)
    private String hotelId;// 酒店唯⼀标识

    @Column(name = "name", nullable = false, length = 100)
    private String name;// 酒店名称

    @Column(name = "destination", nullable = false, length = 50)
    private String destination;// 所属城市或区域

    @Column(name = "address", nullable = false, length = 200)
    private String address;// 详细地址

    @Column(name = "price_per_night", nullable = false, precision = 10, scale = 2)
    private Double pricePerNight;// 每晚基础价格（元）

    @Column(name = "rating", precision = 3, scale = 1)
    private Double rating;// 评分（0–5）

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;// 酒店简介

    @Column(name = "enabled", nullable = false)
    private Boolean enabled = true;// 是否对外可订

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoomType> roomTypes = new ArrayList<>();

}
