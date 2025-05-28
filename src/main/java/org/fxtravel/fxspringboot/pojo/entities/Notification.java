package org.fxtravel.fxspringboot.pojo.entities;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Notification {
    private String id;// 消息ID
    private Integer userId;// 用户ID
    private String eventType; // 消息事件类型TICKET_PURCHASED,ORDER_CANCELLED,PAYMENT_COMPLETED,MEAL_ORDERED,HOTEL_BOOKED
    private String content;// 通知内容
    private String channel; // EMAIL, IN_APP
    private String status; // 发送状态PENDING, SUCCESS, FAILED
    private LocalDateTime createTime;// 创建时间
    private LocalDateTime updateTime;// 更新时间
}
