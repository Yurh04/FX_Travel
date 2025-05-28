package org.fxtravel.fxspringboot.pojo.dto.train;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GetTicketRequest {
    @NotNull(message = "座位ID不能为空")
    private Integer trainSeatId;
}