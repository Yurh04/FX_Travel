package org.fxtravel.fxspringboot.mapper;

import org.fxtravel.fxspringboot.pojo.entities.Hotel;
import org.fxtravel.fxspringboot.pojo.entities.HotelBooking;
import org.fxtravel.fxspringboot.pojo.entities.RoomType;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.util.List;

public interface HotelMapper {

    @Select("SELECT * FROM hotels WHERE destination = #{destination} AND enabled = true")
    List<Hotel> searchAvailableHotels(@Param("destination") String destination);

    @Select("SELECT * FROM hotels WHERE hotel_id = #{hotelId}")
    Hotel findHotelById(@Param("hotelId") String hotelId);

    @Select("SELECT * FROM room_types WHERE room_type_id = #{roomTypeId}")
    RoomType findRoomTypeById(@Param("roomTypeId") String roomTypeId);

    @Select("SELECT * FROM room_types WHERE hotel_id = #{hotelId}")
    List<RoomType> findRoomTypesByHotelId(@Param("hotelId") String hotelId);

    @Insert("""
        INSERT INTO hotel_bookings 
        (booking_id, user_id, hotel_id, room_type_id, check_in_date, check_out_date, 
         status, total_amount, created_at)
        VALUES 
        (#{bookingId}, #{userId}, #{hotelId}, #{roomTypeId}, #{checkInDate}, #{checkOutDate}, 
         #{status}, #{totalAmount}, #{createdAt})
    """)
    @Options(useGeneratedKeys = true, keyProperty = "bookingId")
    void saveBooking(HotelBooking booking);

    @Update("UPDATE room_types SET stock = stock - #{quantity} WHERE room_type_id = #{roomTypeId}")
    void decreaseRoomStock(
            @Param("roomTypeId") String roomTypeId,
            @Param("quantity") int quantity);

    @Update("UPDATE hotel_bookings SET status = 'CANCELED' WHERE booking_id = #{bookingId}")
    void cancelBooking(@Param("bookingId") String bookingId);

    @Update("UPDATE room_types SET stock = stock + #{quantity} WHERE room_type_id = #{roomTypeId}")
    void increaseRoomStock(
            @Param("roomTypeId") String roomTypeId,
            @Param("quantity") int quantity);

    @Select("SELECT * FROM hotel_bookings WHERE user_id = #{userId}")
    List<HotelBooking> findBookingsByUserId(@Param("userId") Integer userId);

    @Select("SELECT * FROM hotel_bookings WHERE booking_id = #{bookingId}")
    HotelBooking findBookingById(@Param("bookingId") String bookingId);

    @Select("""
        SELECT * FROM hotels 
        ORDER BY 
        CASE WHEN #{sortBy} = 'price' THEN price_per_night END #{direction},
        CASE WHEN #{sortBy} = 'rating' THEN rating END #{direction}
        """)
    List<Hotel> findAllSorted(@Param("sortBy") String sortBy, @Param("direction") String direction);
}

