package org.fxtravel.fxspringboot.pojo.entities.trainmeal;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fxtravel.fxspringboot.common.E_MealTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "train_meal")
public class TrainMeal {
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
