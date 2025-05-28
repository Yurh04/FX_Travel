package org.fxtravel.fxspringboot.repository;

import org.fxtravel.fxspringboot.pojo.entities.Train;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TrainRepository extends JpaRepository<Train, Integer> {
    public List<Train> findByFromStationAndToStationAndDepartureTimeBetweenOrderByDepartureTime(String fromStation, String toStation, LocalDateTime startOfDay, LocalDateTime endOfDay);

    public List<Train> findByFromStationAndToStationAndDepartureTimeBetweenOrderByDurationMinutes(String fromStation, String toStation, LocalDateTime startOfDay, LocalDateTime endOfDay);
}
