package org.fxtravel.fxspringboot.pojo.dto.train;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchTrainRequest {
    @NotBlank(message = "出发站不能为空")
    private String departureStation;

    @NotBlank(message = "到达站不能为空")
    private String arrivalStation;

    @NotNull(message = "出发日期不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate departureDate;
}
