package org.fxtravel.fxspringboot.pojo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fxtravel.fxspringboot.common.SeatType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "train_seat")
public class TrainSeat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "train_id")
    private Integer trainId;

    @Enumerated(EnumType.STRING)
    @Column(name = "seat_type", nullable = false)
    private SeatType seatType;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Integer remain;

    @Lob
    @Column(
            name = "seat_allocation",
            columnDefinition = "BINARY(64) DEFAULT 0x00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"
    )
    private byte[] seatAllocation;
}