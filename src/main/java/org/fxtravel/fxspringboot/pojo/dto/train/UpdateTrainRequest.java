package org.fxtravel.fxspringboot.pojo.dto.train;

import lombok.Data;
import org.fxtravel.fxspringboot.common.TrainType;

import java.time.LocalDateTime;

@Data
public class UpdateTrainRequest {
    private String trainNumber;
    private TrainType trainType;
    private String fromStation;
    private String toStation;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
}
