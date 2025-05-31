package org.fxtravel.fxspringboot.pojo.dto.notification;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.fxtravel.fxspringboot.common.E_NotificationChannel;
import org.fxtravel.fxspringboot.common.E_NotificationEventType;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class NotificationRequestDTO {
    @NotNull
    private Integer userId; // 用户ID (必填)
    @NotNull
    private E_NotificationEventType eventType; // 事件类型 (必填)

    private E_NotificationChannel preferredChannel; // 优先渠道 (可选)

    private String orderId; // 关联订单ID (可选)

    private BigDecimal amount; // 金额 (可选)

    private Map<String, Object> details; // 其他详情 (可选)
}