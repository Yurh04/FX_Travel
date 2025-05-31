package org.fxtravel.fxspringboot.controller;

import lombok.RequiredArgsConstructor;
import org.fxtravel.fxspringboot.pojo.dto.hotel.HotelBookingRequestDTO;
import org.fxtravel.fxspringboot.pojo.entities.Hotel;
import org.fxtravel.fxspringboot.pojo.entities.HotelBooking;
import org.fxtravel.fxspringboot.service.inter.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/hotels")
@RequiredArgsConstructor
public class HotelController {
    @Autowired
    private final HotelService hotelService;

    @GetMapping
    public ResponseEntity<List<Hotel>> searchHotels(
            @RequestParam String destination,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate) {
        List<Hotel> hotels = hotelService.searchHotels(destination, "price", true);
        return ResponseEntity.ok(hotels);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Hotel>> advancedSearch(
            @RequestParam String destination,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate,
            @RequestParam(defaultValue = "price") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder) {
        boolean ascending = "asc".equalsIgnoreCase(sortOrder);
        List<Hotel> hotels = hotelService.getHotelsSorted(sortBy, ascending);
        return ResponseEntity.ok(hotels);
    }

    @PostMapping("/bookings")
    public ResponseEntity<HotelBooking> bookHotel(
            @RequestBody HotelBookingRequestDTO request,
            @RequestHeader("X-User-Id") Integer userId) {
        HotelBooking booking = hotelService.bookHotel(
                userId,
                request.getHotelId(),
                request.getRoomTypeId(),
                request.getCheckInDate(),
                request.getCheckOutDate());
        return ResponseEntity.status(HttpStatus.CREATED).body(booking);
    }

    @GetMapping("/bookings")
    public ResponseEntity<List<HotelBooking>> getUserBookings(
            @RequestHeader("X-User-Id") Integer userId) {
        List<HotelBooking> bookings = hotelService.getUserBookings(userId);
        return ResponseEntity.ok(bookings);
    }

    @DeleteMapping("/bookings/{bookingId}")
    public ResponseEntity<Void> cancelBooking(
            @PathVariable String bookingId,
            @RequestHeader("X-User-Id") Integer userId) {
        hotelService.cancelBooking(bookingId, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{hotelId}")
    public ResponseEntity<Hotel> getHotelDetails(@PathVariable String hotelId) {
        Hotel hotel = hotelService.getHotelDetails(hotelId);
        return ResponseEntity.ok(hotel);
    }
}
