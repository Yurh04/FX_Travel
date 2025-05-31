package org.fxtravel.fxspringboot.pojo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class RoomType {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "room_type_id", columnDefinition = "VARCHAR(36)", nullable = false)
    private String roomTypeId;//房间唯一标识

    @Column(name = "hotel_id", nullable = false, length = 36)
    private String hotelId;//关联酒店id

    @Column(name = "name", nullable = false, length = 50)
    private String name;// 房型名称（如“⼤床房”）

    @Column(name = "stock", nullable = false)
    private Integer stock;// 剩余可售房量

    @Column(name = "price_multiplier", nullable = false, precision = 3, scale = 2)
    private Double priceMultiplier;//相对基础价的倍数（如 1.2 表示 pricePerNight*1.2）
}
