package org.fxtravel.fxspringboot.pojo.dto.payment;

import lombok.Data;
import org.fxtravel.fxspringboot.common.E_PaymentStatus;

@Data
public class PaymentResultDTO {
    private String orderNumber;
    private E_PaymentStatus currentStatus;
    private String message;
    private Long remainingTimeSeconds;
}
