package org.fxtravel.fxspringboot.pojo.dto.train;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GetTicketRequest {
    private Integer userId;
    private Integer seatId;
}