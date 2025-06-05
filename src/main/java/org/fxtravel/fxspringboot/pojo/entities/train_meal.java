package org.fxtravel.fxspringboot.pojo.entities;

import jakarta.persistence.*;
import jdk.jfr.Unsigned;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fxtravel.fxspringboot.common.E_MealTime;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class train_meal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer trainId;
    private String name;
    private String description;
    private String image;
    @Enumerated(EnumType.STRING)
    private E_MealTime mealTime;
    private Double price;
    private Integer remain;
    private Boolean enabled;
}
