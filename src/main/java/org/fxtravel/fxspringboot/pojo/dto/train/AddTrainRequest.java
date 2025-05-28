package org.fxtravel.fxspringboot.pojo.dto.train;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.fxtravel.fxspringboot.common.TrainType;

import java.time.LocalDateTime;

@Data
public class AddTrainRequest {
    @NotBlank(message = "车次号不能为空")
    private String trainNumber;

    @NotNull(message = "列车类型不能为空")
    private TrainType trainType;

    @NotBlank(message = "出发站不能为空")
    private String fromStation;

    @NotBlank(message = "到达站不能为空")
    private String toStation;

    @NotNull(message = "出发时间不能为空")
    private LocalDateTime departureTime;

    @NotNull(message = "到达时间不能为空")
    private LocalDateTime arrivalTime;
}
