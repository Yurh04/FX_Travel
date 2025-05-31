package org.fxtravel.fxspringboot.mapper;

import org.fxtravel.fxspringboot.pojo.entities.Hotel;
import org.fxtravel.fxspringboot.pojo.entities.HotelBooking;
import org.fxtravel.fxspringboot.pojo.entities.RoomType;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.util.List;

public interface HotelMapper {

    // 酒店搜索
    @Select("""
        SELECT * FROM hotels 
        WHERE destination = #{destination} 
        AND enabled = true
        AND hotel_id IN (
            SELECT DISTINCT hotel_id FROM room_types 
            WHERE stock > 0
        )
        ORDER BY ${sortField} ${direction}
    """)
    List<Hotel> searchAvailableHotels(
            @Param("destination") String destination,
            @Param("sortField") String sortField,
            @Param("direction") String direction);

    // 根据ID获取酒店
    @Select("SELECT * FROM hotels WHERE hotel_id = #{hotelId}")
    Hotel findHotelById(@Param("hotelId") String hotelId);

    // 获取房型
    @Select("SELECT * FROM room_types WHERE room_type_id = #{roomTypeId}")
    RoomType findRoomTypeById(@Param("roomTypeId") String roomTypeId);

    // 创建预订
    @Insert("""
        INSERT INTO hotel_bookings 
        (booking_id, user_id, hotel_id, room_type_id, check_in_date, check_out_date, 
         status, total_amount, created_at, payment_id)
        VALUES 
        (#{bookingId}, #{userId}, #{hotelId}, #{roomTypeId}, #{checkInDate}, #{checkOutDate}, 
         #{status}, #{totalAmount}, #{createdAt}, #{paymentId})
    """)
    void saveBooking(HotelBooking booking);

    // 更新房型库存
    @Update("UPDATE room_types SET stock = stock - #{quantity} WHERE room_type_id = #{roomTypeId}")
    void decreaseRoomStock(
            @Param("roomTypeId") String roomTypeId,
            @Param("quantity") int quantity);

    // 取消预订
    @Update("UPDATE hotel_bookings SET status = 'CANCELED' WHERE booking_id = #{bookingId}")
    void cancelBooking(@Param("bookingId") String bookingId);

    // 恢复库存
    @Update("UPDATE room_types SET stock = stock + #{quantity} WHERE room_type_id = #{roomTypeId}")
    void increaseRoomStock(
            @Param("roomTypeId") String roomTypeId,
            @Param("quantity") int quantity);

    // 获取用户所有预订
    @Select("SELECT * FROM hotel_bookings WHERE user_id = #{userId}")
    List<HotelBooking> findBookingsByUserId(@Param("userId") int userId);
}
