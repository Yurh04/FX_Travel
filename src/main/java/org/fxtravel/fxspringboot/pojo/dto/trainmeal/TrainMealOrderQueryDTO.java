package org.fxtravel.fxspringboot.pojo.dto.trainmeal;

import jakarta.annotation.Nullable;
import lombok.Data;
import org.fxtravel.fxspringboot.common.E_PaymentStatus;

import java.time.LocalDateTime;

// 列车餐订单查询DTO
@Data
public class TrainMealOrderQueryDTO {
    @Nullable
    private Integer userId;

    @Nullable
    private Integer ticketReservationId;

    @Nullable
    private Integer trainMealId;

    @Nullable
    private E_PaymentStatus status;

    @Nullable
    private LocalDateTime createTimeStart;

    @Nullable
    private LocalDateTime createTimeEnd;
}
