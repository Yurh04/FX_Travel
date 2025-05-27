package org.fxtravel.fxspringboot.pojo.entities;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Hotel {
    private String hotelId;// 酒店唯⼀标识
    private String name;// 酒店名称
    private String destination; // 所属城市或区域
    private String address; // 详细地址
    private Double pricePerNight;// 每晚基础价格（元）
    private Double rating;// 评分（0–5）
    private String description;// 酒店简介
    private Boolean enabled;// 是否对外可订

}
