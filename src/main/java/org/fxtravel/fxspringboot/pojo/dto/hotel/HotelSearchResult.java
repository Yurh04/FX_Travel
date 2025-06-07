package org.fxtravel.fxspringboot.pojo.dto.hotel;

import lombok.Data;
import org.fxtravel.fxspringboot.pojo.entities.Hotel;
import org.fxtravel.fxspringboot.pojo.entities.Room;

import java.util.List;

@Data
public class HotelSearchResult {
    private Hotel hotel;
    private List<Room> rooms;
}