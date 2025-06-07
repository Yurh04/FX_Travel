package org.fxtravel.fxspringboot.pojo.dto.hotel;

import jakarta.annotation.Nullable;
import lombok.Data;

@Data
public class SearchHotelRequest {
    String destination;
    @Nullable
    String namePattern;     // 空就是任意
}
