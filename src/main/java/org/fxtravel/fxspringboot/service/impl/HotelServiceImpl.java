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
    private HotelMapper hotelMapper;
    @Autowired
    private NotificationService notificationService;

    @Override
    public List<Hotel> searchHotels(String destination, String sortBy, boolean ascending) {
        List<Hotel> hotels = hotelMapper.searchAvailableHotels(destination);
        hotels.forEach(hotel ->
                hotel.setRoomTypes(hotelMapper.findRoomTypesByHotelId(hotel.getHotelId())));
        return hotels;
    }

    @Override
    @Transactional
    public HotelBooking bookHotel(Integer userId, String hotelId, String roomTypeId,
                                  LocalDate checkIn, LocalDate checkOut) {
        RoomType roomType = hotelMapper.findRoomTypeById(roomTypeId);
        if (roomType == null || roomType.getStock() <= 0) {
            throw new IllegalStateException("Room not available");
        }

        Hotel hotel = hotelMapper.findHotelById(hotelId);
        if (hotel == null || !hotel.getEnabled()) {
            throw new IllegalStateException("Hotel not available");
        }

        long nights = checkIn.until(checkOut).getDays();
        double totalAmount = hotel.getPricePerNight() * roomType.getPriceMultiplier() * nights;

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

        NotificationRequestDTO notification = new NotificationRequestDTO();
        notification.setUserId(userId);
        notification.setEventType(E_NotificationEventType.HOTEL_BOOKED);
        notification.setOrderId(booking.getBookingId());
        notificationService.createNotification(notification);
        return booking;
    }

    @Override
    @Transactional
    public void cancelBooking(String bookingId, Integer userId) {
        HotelBooking booking = hotelMapper.findBookingById(bookingId);
        if (booking == null || booking.getUserId()!=userId) {
            throw new IllegalArgumentException("Invalid booking ID");
        }

        if (!E_HotelBookingStatus.CONFIRMED.toString().equals(booking.getStatus())) {
            throw new IllegalStateException("Only confirmed bookings can be canceled");
        }

        hotelMapper.cancelBooking(bookingId);
        hotelMapper.increaseRoomStock(booking.getRoomTypeId(), 1);

        NotificationRequestDTO notification = new NotificationRequestDTO();
        notification.setUserId(userId);
        notification.setEventType(E_NotificationEventType.ORDER_CANCELLED);
        notification.setOrderId(bookingId);
        notificationService.createNotification(notification);
    }

    @Override
    public List<HotelBooking> getUserBookings(Integer userId) {
        return hotelMapper.findBookingsByUserId(userId);
    }

    @Override
    public Hotel getHotelDetails(String hotelId) {
        Hotel hotel = hotelMapper.findHotelById(hotelId);
        if (hotel != null) {
            hotel.setRoomTypes(hotelMapper.findRoomTypesByHotelId(hotelId));
        }
        return hotel;
    }

    @Override
    public List<Hotel> getHotelsSorted(String sortBy, boolean ascending) {
        String direction = ascending ? "ASC" : "DESC";
        return hotelMapper.findAllSorted(sortBy, direction);
    }
}