package org.fxtravel.fxspringboot.controller.trainseat;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.fxtravel.fxspringboot.common.E_PaymentType;
import org.fxtravel.fxspringboot.common.Role;
import org.fxtravel.fxspringboot.pojo.dto.payment.PaymentResultDTO;
import org.fxtravel.fxspringboot.pojo.dto.train.GetTicketRequest;
import org.fxtravel.fxspringboot.pojo.dto.train.SearchTrainRequest;
import org.fxtravel.fxspringboot.pojo.dto.train.TrainSearchResult;
import org.fxtravel.fxspringboot.pojo.entities.TrainSeatOrder;
import org.fxtravel.fxspringboot.pojo.entities.User;
import org.fxtravel.fxspringboot.pojo.entities.payment;
import org.fxtravel.fxspringboot.pojo.entities.train_meal_order;
import org.fxtravel.fxspringboot.service.inter.PaymentService;
import org.fxtravel.fxspringboot.service.inter.TrainSeatOrderService;
import org.fxtravel.fxspringboot.service.inter.TrainSeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import org.springframework.validation.FieldError;
@RestController
@RequestMapping("/api/train")
public class TrainSeatOrderController {
    @Autowired
    private TrainSeatOrderService trainSeatOrderService;
    @Autowired
    private PaymentService paymentService;

    // 根据座次生成车票接口
    @PostMapping("/ticket/get")
    public ResponseEntity<?> getTicket(@Valid @RequestBody GetTicketRequest request,
                                       BindingResult bindingResult,
                                       HttpSession session) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(Map.of("errors", errors));
        }

        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "未登录"));
        }

        // 验证用户只能为自己生成车票（除非是管理员）
        if (!user.getRole().equals(Role.ADMIN) && user.getId() != (request.getUserId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "无权限为其他用户生成车票"));
        }

        try {
            TrainSeatOrder order = trainSeatOrderService.createOrder(request);

            return ResponseEntity.ok(Map.of(
                    "message", "车票生成成功",
                    "id", order.getId(),
                    "number", order.getSeatNumber()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "生成车票失败: " + e.getMessage()));
        }
    }

    @GetMapping("/ticket/{orderId}")
    public ResponseEntity<PaymentResultDTO> getOrderPaymentStatus(@PathVariable Integer orderId) {
        TrainSeatOrder order = trainSeatOrderService.getOrderById(orderId);
        if (order == null) {
            return ResponseEntity.notFound().build();
        }

        PaymentResultDTO result = order.getRelatedPaymentId() != null ?
                paymentService.checkPaymentStatus(order.getRelatedPaymentId()) : null;

        return ResponseEntity.ok(result);
    }
}
