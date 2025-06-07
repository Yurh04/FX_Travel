package org.fxtravel.fxspringboot.pojo.dto.hotel;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BookHotelRequest {
    @NotNull
    private Integer hotelId;
    @NotNull
    private Integer roomId;
    @NotNull
    private Integer userId;

    @NotNull(message = "入住日期不能为空")
    @FutureOrPresent(message = "入住日期必须是当前或将来日期")
    private LocalDate checkInDate;

    @NotNull(message = "离店日期不能为空")
    @Future(message = "离店日期必须是将来日期")
    private LocalDate checkOutDate;
}