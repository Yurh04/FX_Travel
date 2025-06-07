package org.fxtravel.fxspringboot.pojo.dto.train;

import lombok.Data;
import org.fxtravel.fxspringboot.pojo.entities.Train;
import org.fxtravel.fxspringboot.pojo.entities.TrainSeat;

import java.util.List;

@Data
public class TrainSearchResult {
    private Train train;                    // 列车信息
    private List<TrainSeat> trainseats;     // 该列车的所有座位信息
}