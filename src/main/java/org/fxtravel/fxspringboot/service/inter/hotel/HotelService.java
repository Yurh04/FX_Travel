package org.fxtravel.fxspringboot.service.inter.hotel;

import com.baomidou.mybatisplus.extension.service.IService;
import org.fxtravel.fxspringboot.pojo.dto.hotel.HotelSearchResult;
import org.fxtravel.fxspringboot.pojo.entities.Hotel;
import org.fxtravel.fxspringboot.pojo.entities.Room;
import org.fxtravel.fxspringboot.pojo.entities.RoomOrder;
import org.fxtravel.fxspringboot.service.inter.common.GoodService;

import java.time.LocalDate;
import java.util.List;

public interface HotelService extends GoodService {
    Hotel getHotelById(Integer id);
    Room getRoomById(Integer id);
    List<HotelSearchResult> searchHotels(String destination, String pattern);
}