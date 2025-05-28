package org.fxtravel.fxspringboot.pojo.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class TrainTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // 座次关联（外键）
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "train_seat_id", nullable = false)
    private TrainSeat trainSeat;

    // 用户关联（外键）
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
