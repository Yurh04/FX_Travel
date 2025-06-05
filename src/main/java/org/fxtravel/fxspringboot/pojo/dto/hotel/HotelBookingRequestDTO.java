package org.fxtravel.fxspringboot.pojo.dto.hotel;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
public class HotelBookingRequestDTO {
    @NotBlank(message = "酒店ID不能为空")
    @Size(min = 1, max = 36, message = "酒店ID长度必须在1-36个字符之间")
    private String hotelId;

    @NotBlank(message = "房型ID不能为空")
    @Size(min = 1, max = 36, message = "房型ID长度必须在1-36个字符之间")
    private String roomTypeId;

    @NotNull(message = "入住日期不能为空")
    @FutureOrPresent(message = "入住日期必须是当前或将来日期")
    private LocalDate checkInDate;

    @NotNull(message = "离店日期不能为空")
    @Future(message = "离店日期必须是将来日期")
    private LocalDate checkOutDate;
}