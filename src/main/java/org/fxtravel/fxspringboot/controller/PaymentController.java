package org.fxtravel.fxspringboot.controller;

import org.fxtravel.fxspringboot.common.E_PaymentStatus;
import org.fxtravel.fxspringboot.common.E_PaymentType;
import org.fxtravel.fxspringboot.pojo.dto.payment.PaymentDTO;
import org.fxtravel.fxspringboot.pojo.dto.payment.PaymentQueryDTO;
import org.fxtravel.fxspringboot.pojo.dto.payment.PaymentRequest;
import org.fxtravel.fxspringboot.pojo.dto.payment.PaymentResultDTO;
import org.fxtravel.fxspringboot.pojo.entities.payment;
import org.fxtravel.fxspringboot.service.inter.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/complete")
    public ResponseEntity<Boolean> completePayment(@RequestBody PaymentRequest request) {
        boolean result = paymentService.completePayment(request.getOrderNumber(), request.getData());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/fail")
    public ResponseEntity<Boolean> cancelPayment(@RequestBody PaymentRequest request) {
        boolean result = paymentService.failPayment(request.getOrderNumber(), request.getData());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/refund")
    public ResponseEntity<Boolean> refundPayment(@RequestBody PaymentRequest request) {
        boolean result = paymentService.refundPayment(request.getOrderNumber(), request.getData());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/finish")
    public ResponseEntity<Boolean> finishPayment(@RequestBody PaymentRequest request) {
        boolean result = paymentService.finishPayment(request.getOrderNumber(), request.getData());
        return ResponseEntity.ok(result);
    }
}