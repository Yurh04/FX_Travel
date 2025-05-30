package org.fxtravel.fxspringboot.pojo.entities;

import jakarta.persistence.Column;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fxtravel.fxspringboot.common.E_NotificationChannel;
import org.fxtravel.fxspringboot.common.E_NotificationEventType;
import org.fxtravel.fxspringboot.common.E_NotificationStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Notification {
    @Id
    @Column(name = "id", length = 36, nullable = false)
    private String id; // 消息ID

    // 关联用户ID（外键）
    @Column(name = "user_id", nullable = false)
    private Integer userId;// 用户ID

    // 事件类型（枚举）
    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", length = 20, nullable = false)
    private E_NotificationEventType eventType;// 消息事件类型TICKET_PURCHASED,ORDER_CANCELLED,PAYMENT_COMPLETED,MEAL_ORDERED,HOTEL_BOOKED

    // 通知内容
    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;// 通知内容

    // 通知渠道（枚举）
    @Enumerated(EnumType.STRING)
    @Column(name = "channel", length = 10, nullable = false)
    private E_NotificationChannel channel;// EMAIL, IN_APP

    // 发送状态（枚举）
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 10, nullable = false)
    private E_NotificationStatus status;// 发送状态PENDING, SUCCESS, FAILED

    // 创建时间
    @Column(name = "create_time", nullable = false, updatable = false)
    private LocalDateTime createTime;// 创建时间

    // 更新时间
    @Column(name = "update_time")
    private LocalDateTime updateTime;// 更新时间

    // 关联订单ID（可选）
    @Column(name = "related_order_id", length = 36)
    private String relatedOrderId;

    // 是否已读
    @Column(name = "is_read", nullable = false)
    private Boolean isRead = false;

    // 重试次数
    @Column(name = "retry_count", nullable = false)
    private Integer retryCount = 0;
}
