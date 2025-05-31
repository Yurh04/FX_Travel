package org.fxtravel.fxspringboot.service.impl;

import lombok.RequiredArgsConstructor;
import org.fxtravel.fxspringboot.common.E_HotelBookingStatus;
import org.fxtravel.fxspringboot.common.E_NotificationEventType;
import org.fxtravel.fxspringboot.mapper.HotelMapper;
import org.fxtravel.fxspringboot.pojo.dto.notification.NotificationRequestDTO;
import org.fxtravel.fxspringboot.pojo.entities.Hotel;
import org.fxtravel.fxspringboot.pojo.entities.HotelBooking;
import org.fxtravel.fxspringboot.pojo.entities.RoomType;
import org.fxtravel.fxspringboot.service.inter.HotelService;
import org.fxtravel.fxspringboot.service.inter.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {
    @Autowired
    private final HotelMapper hotelMapper;
    @Autowired
    private final NotificationService notificationService;

    @Override
    public List<Hotel> searchHotels(String destination, String sortBy, boolean ascending) {
        String validSortField = "price".equals(sortBy) ? "price_per_night" : "rating";
        String direction = ascending ? "ASC" : "DESC";
        return hotelMapper.searchAvailableHotels(destination, validSortField, direction);
    }

    @Override
    @Transactional
    public HotelBooking bookHotel(Integer userId, String hotelId, String roomTypeId,
                                  LocalDate checkIn, LocalDate checkOut) {
        // 1. 检查房型可用性
        RoomType roomType = hotelMapper.findRoomTypeById(roomTypeId);
        if (roomType == null || roomType.getStock() <= 0) {
            throw new IllegalStateException("该房型不可预订");
        }

        // 2. 获取酒店信息
        Hotel hotel = hotelMapper.findHotelById(hotelId);
        if (hotel == null || !hotel.getEnabled()) {
            throw new IllegalStateException("酒店不可用");
        }

        // 3. 计算总价
        long nights = checkIn.until(checkOut).getDays();
        double totalAmount = hotel.getPricePerNight() * roomType.getPriceMultiplier() * nights;

        // 4. 创建预订记录
        HotelBooking booking = new HotelBooking();
        booking.setBookingId(UUID.randomUUID().toString());
        booking.setUserId(userId);
        booking.setHotelId(hotelId);
        booking.setRoomTypeId(roomTypeId);
        booking.setCheckInDate(checkIn);
        booking.setCheckOutDate(checkOut);
        booking.setStatus(E_HotelBookingStatus.CONFIRMED);
        booking.setTotalAmount(totalAmount);
        booking.setCreatedAt(LocalDateTime.now());

        hotelMapper.saveBooking(booking);
        hotelMapper.decreaseRoomStock(roomTypeId, 1);

        // 5. 发送通知 - 使用标准通知接口
        NotificationRequestDTO notificationRequest = new NotificationRequestDTO();
        notificationRequest.setUserId(userId);
        notificationRequest.setEventType(E_NotificationEventType.HOTEL_BOOKED);
        notificationRequest.setOrderId(booking.getBookingId());
        notificationService.createNotification(notificationRequest);

        return booking;
    }

    @Override
    @Transactional
    public void cancelBooking(String bookingId, Integer userId) {
        HotelBooking booking = hotelMapper.findBookingsByUserId(userId).stream()
                .filter(b -> bookingId.equals(b.getBookingId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("无效的预订ID"));

        if (!E_HotelBookingStatus.CONFIRMED.toString().equals(booking.getStatus())) {
            throw new IllegalStateException("只有已确认的预订可以取消");
        }

        hotelMapper.cancelBooking(bookingId);
        hotelMapper.increaseRoomStock(booking.getRoomTypeId(), 1);

        // 发送取消通知
        NotificationRequestDTO notificationRequest = new NotificationRequestDTO();
        notificationRequest.setUserId(userId);
        notificationRequest.setEventType(E_NotificationEventType.ORDER_CANCELLED);
        notificationRequest.setOrderId(booking.getBookingId());
        notificationService.createNotification(notificationRequest);
    }
    @Override
    public List<HotelBooking> getUserBookings(Integer userId) {
        return hotelMapper.findBookingsByUserId(userId);
    }

    @Override
    public Hotel getHotelDetails(String hotelId) {
        return hotelMapper.findHotelById(hotelId);
    }
}