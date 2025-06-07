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
@Table(name = "hotel")
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;// 酒店唯⼀标识

    @Column(name = "name", nullable = false)
    private String name;// 酒店名称

    @Column(name = "destination", nullable = false)
    private String destination;// 所属城市或区域

    @Column(name = "address", nullable = false)
    private String address;// 详细地址


    @Column(name = "rating")
    private Double rating;// 评分（0–5）

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;// 酒店简介
}
