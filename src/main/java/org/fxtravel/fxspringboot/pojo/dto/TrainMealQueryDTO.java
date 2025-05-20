package org.fxtravel.fxspringboot.pojo.dto;

import lombok.Data;

@Data
public class TrainMealQueryDTO {
    Integer trainId;
    String name;
    String mealTime;
    Double priceMin;
    Double priceMax;
    Boolean remain;
    Boolean enabled;
}
