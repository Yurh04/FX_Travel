package org.fxtravel.fxspringboot.pojo.dto.train;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.fxtravel.fxspringboot.common.E_PaymentStatus;
import org.fxtravel.fxspringboot.pojo.entities.Train;
import org.fxtravel.fxspringboot.pojo.entities.TrainSeat;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TrainSeatOrderDTO {
    private Integer id;
    private Integer userId;
    private Train train;
    private TrainSeat trainSeat;
    private String seatNumber;
    private Integer relatedPaymentId;
    private Double totalAmount;
    private E_PaymentStatus status;
    private LocalDateTime createTime;

}
