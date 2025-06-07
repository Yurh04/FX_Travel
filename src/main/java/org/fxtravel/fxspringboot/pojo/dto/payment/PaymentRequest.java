package org.fxtravel.fxspringboot.pojo.dto.payment;

import lombok.Data;

@Data
public class PaymentRequest {
    private String orderNumber;
    private Object data;
}
