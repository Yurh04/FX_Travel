package org.fxtravel.fxspringboot.service.inter;

import org.fxtravel.fxspringboot.pojo.entities.Hotel;
import org.fxtravel.fxspringboot.pojo.entities.HotelBooking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface HotelService {

    // 酒店搜索
    List<Hotel> searchHotels(String destination, String sortBy, boolean ascending);

    // 酒店预订
    HotelBooking bookHotel(Integer userId, String hotelId, String roomTypeId,
                           LocalDate checkIn, LocalDate checkOut);

    // 取消预订
    void cancelBooking(String bookingId,Integer userId);

    // 获取用户预订记录
    List<HotelBooking> getUserBookings(Integer userId);

    // 获取酒店详情
    Hotel getHotelDetails(String hotelId);
}