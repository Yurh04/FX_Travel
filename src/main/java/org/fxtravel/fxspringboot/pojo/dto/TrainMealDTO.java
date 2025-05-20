package org.fxtravel.fxspringboot.pojo.dto;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fxtravel.fxspringboot.common.E_MealTime;

@Data
public class TrainMealDTO {
    @Nullable
    private Integer id;
    private Integer trainId;
    private String name;
    private String description;
    private String image;
    private E_MealTime mealTime;
    private Double price;
    @Nullable
    private Integer remain;
    @Nullable
    private Boolean enabled;
}