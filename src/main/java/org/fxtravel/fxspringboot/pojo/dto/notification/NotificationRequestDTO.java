package org.fxtravel.fxspringboot.pojo.dto.notification;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class NotificationRequestDTO {
    private Integer userId; // 用户ID (必填)
    private String eventType; // 事件类型 (必填)
    private String preferredChannel; // 优先渠道 (可选)
    private String orderId; // 关联订单ID (可选)
    private BigDecimal amount; // 金额 (可选)
    private Map<String, Object> details; // 其他详情 (可选)
}