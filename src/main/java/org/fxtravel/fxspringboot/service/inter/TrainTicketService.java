package org.fxtravel.fxspringboot.service.inter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.fxtravel.fxspringboot.pojo.dto.train.TrainSearchResult;
import org.fxtravel.fxspringboot.pojo.entities.TrainTicket;
import org.fxtravel.fxspringboot.pojo.entities.User;

import java.time.LocalDate;
import java.util.List;

public interface TrainTicketService {

    TrainTicket generateTicket(User user, Integer trainSeatId);
    TrainTicket cancelTicket(Integer ticketId, Integer userId);
    List<TrainSearchResult> searchTrainsByDepartureTime(String fromStation, String toStation, LocalDate departureDate);
    List<TrainSearchResult> searchTrainsByTravelDuration(String fromStation, String toStation, LocalDate departureDate);


}
