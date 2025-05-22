package org.fxtravel.fxspringboot.controller;

import org.fxtravel.fxspringboot.common.E_PaymentStatus;
import org.fxtravel.fxspringboot.common.E_PaymentType;
import org.fxtravel.fxspringboot.pojo.dto.PaymentDTO;
import org.fxtravel.fxspringboot.pojo.dto.PaymentQueryDTO;
import org.fxtravel.fxspringboot.pojo.dto.PaymentResultDTO;
import org.fxtravel.fxspringboot.pojo.entities.payment;
import org.fxtravel.fxspringboot.service.inter.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    // -------------------- 管理员查询接口 --------------------
    @GetMapping("/{id}")
    public ResponseEntity<payment> getPaymentById(@PathVariable Integer id) {
        payment payment = paymentService.getPaymentById(id);
        return ResponseEntity.ok(payment);
    }

    @GetMapping("/order/{orderNumber}")
    public ResponseEntity<payment> getPaymentByOrderNumber(@PathVariable String orderNumber) {
        payment payment = paymentService.getPaymentByOrderNumber(orderNumber);
        return ResponseEntity.ok(payment);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<payment>> getPaymentsByType(@PathVariable E_PaymentType type) {
        List<payment> payments = paymentService.getPaymentsByType(type);
        return ResponseEntity.ok(payments);
    }

    @PostMapping("/query")
    public ResponseEntity<List<payment>> queryPayments(@RequestBody PaymentQueryDTO queryDTO) {
        List<payment> payments = paymentService.queryPayments(queryDTO);
        return ResponseEntity.ok(payments);
    }

    // -------------------- 支付操作接口 --------------------
    @PostMapping
    public ResponseEntity<payment> createPayment(@RequestBody PaymentDTO paymentDTO) {
        payment payment = paymentService.createPayment(
                paymentDTO.getUserId(),
                paymentDTO.getType(),
                paymentDTO.getAmount(),
                paymentDTO.getRelatedId());
        return ResponseEntity.ok(payment);
    }

    @PostMapping("/complete/{orderNumber}")
    public ResponseEntity<Boolean> completePayment(@PathVariable String orderNumber) {
        boolean result = paymentService.completePayment(orderNumber);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/fail/{orderNumber}")
    public ResponseEntity<Boolean> cancelPayment(@PathVariable String orderNumber) {
        boolean result = paymentService.failPayment(orderNumber);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/refund/{orderNumber}")
    public ResponseEntity<Boolean> refundPayment(@PathVariable String orderNumber) {
        boolean result = paymentService.refundPayment(orderNumber);
        return ResponseEntity.ok(result);
    }

    // -------------------- 统计接口 --------------------
    @GetMapping("/stats/{type}")
    public ResponseEntity<Double> sumAmountByType(@PathVariable E_PaymentType type) {
        Double total = paymentService.sumAmountByType(type);
        return ResponseEntity.ok(total);
    }

    // -------------------- 异步支付接口 --------------------
    @PostMapping("/async/{orderNumber}")
    public ResponseEntity<PaymentResultDTO> processPaymentAsync(
            @PathVariable String orderNumber,
            @RequestParam(defaultValue = "30") long timeoutSeconds) {
        PaymentResultDTO result = paymentService.simulatePaymentProcess(orderNumber, timeoutSeconds);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/async/{orderNumber}")
    public ResponseEntity<E_PaymentStatus> getAsyncStatus(@PathVariable String orderNumber) {
        E_PaymentStatus result = paymentService.checkPaymentStatus(orderNumber);
        return ResponseEntity.ok(result);
    }
}