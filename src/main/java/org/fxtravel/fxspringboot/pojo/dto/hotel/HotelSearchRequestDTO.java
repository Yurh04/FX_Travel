package org.fxtravel.fxspringboot.pojo.dto.hotel;

@Data
public class HotelSearchRequestDTO {
    private String destination;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private String sortBy = "price";
    private String sortOrder = "asc";
}
