package org.fxtravel.fxspringboot.pojo.entities;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class RoomType {
    private String roomTypeId;// 房型唯⼀标识
    private String hotelId;//对应酒店唯一标识
    private String name;// 房型名称（如“⼤床房”）
    private Integer stock;// 剩余可售房量
    private Double priceMultiplier;//相对基础价的倍数（如 1.2 表示 pricePerNight*1.2）
}
