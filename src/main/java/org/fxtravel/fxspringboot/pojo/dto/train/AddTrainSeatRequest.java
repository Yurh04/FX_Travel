package org.fxtravel.fxspringboot.pojo.dto.train;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.fxtravel.fxspringboot.common.SeatType;

import java.math.BigDecimal;

@Data
public class AddTrainSeatRequest {
    @NotNull(message = "车次ID不能为空")
    @Positive(message = "车次ID必须是正整数")
    private Integer trainId; // 对应 TrainSeat.train.id 的外键值

    @NotNull(message = "座位类型不能为空")
    private SeatType seatType;

    @NotNull(message = "票价不能为空")
    @DecimalMin(value = "0.0", inclusive = false, message = "票价必须大于0")
    @Digits(integer = 10, fraction = 2, message = "票价格式不合法（整数位10位，小数位2位）")
    private BigDecimal price;

    @NotNull(message = "剩余票数不能为空")
    @Min(value = 0, message = "剩余票数不能为负数")
    private Integer available;
}
