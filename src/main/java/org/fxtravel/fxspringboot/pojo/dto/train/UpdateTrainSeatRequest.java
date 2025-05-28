package org.fxtravel.fxspringboot.pojo.dto.train;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateTrainSeatRequest {
    @DecimalMin(value = "0.0", inclusive = false, message = "票价必须大于0")
    @Digits(integer = 10, fraction = 2, message = "票价格式不合法（整数位10位，小数位2位）")
    private BigDecimal price;

    @Min(value = 0, message = "剩余票数不能为负数")
    private Integer available;
}
