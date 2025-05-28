package org.fxtravel.fxspringboot.pojo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fxtravel.fxspringboot.common.TrainType;

import java.time.LocalDateTime;
import java.time.Duration;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Train {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String trainNumber;
    @Enumerated(EnumType.STRING)
    private TrainType trainType;
    private String fromStation;
    private String toStation;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;

    private Integer durationMinutes;

    @PrePersist
    @PreUpdate
    private void calculateDuration() {
        if (departureTime != null && arrivalTime != null) {
            Duration duration = Duration.between(departureTime, arrivalTime);
            this.durationMinutes = (int) duration.toMinutes();
        } else {
            this.durationMinutes = null;
        }
    }

}
