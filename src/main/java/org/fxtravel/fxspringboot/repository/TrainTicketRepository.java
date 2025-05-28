package org.fxtravel.fxspringboot.repository;

import org.fxtravel.fxspringboot.pojo.entities.TrainSeat;
import org.fxtravel.fxspringboot.pojo.entities.TrainTicket;
import org.fxtravel.fxspringboot.pojo.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainTicketRepository extends JpaRepository<TrainTicket, Integer> {
    boolean existsByTrainSeatAndUser(TrainSeat trainSeat, User user);
}
