package com.hotel.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.hotel.booking.entity.Booking;
import com.hotel.enums.RoomType;
import com.hotel.room.entity.Room;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    Optional<Room> findByRoomNumber(String roomNumber);
    @Query("SELECT r FROM Room r WHERE r.roomPrice.roomType = :roomType " +
            "AND NOT EXISTS (" +
            "    SELECT b FROM Booking b JOIN b.totalRooms br " +
            "    WHERE br.roomNumber = r.roomNumber " +
            "    AND (b.startDate < :endDate AND b.endDate > :startDate)" +
            ")")
    List<Room> findAvailableRoomsByTypeAndDates(@Param("roomType") RoomType roomType,
                                                @Param("startDate") Date startDate,
                                                @Param("endDate") Date endDate);
    @Query("""
        SELECT r FROM Room r 
        WHERE r.roomPrice.roomType = :roomType 
        AND NOT EXISTS (
            SELECT b FROM Booking b JOIN b.totalRooms br 
            WHERE br.roomNumber = r.roomNumber 
            AND b.bookingId != :bookingId 
            AND b.startDate < :endDate AND b.endDate > :startDate
        )
    """)
    List<Room> findAvailableRoomsByTypeAndDates(
            @Param("roomType") RoomType roomType,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate,
            @Param("bookingId") Long bookingId
    );

    @Query(value = "SELECT count(*)\n" +
            "FROM hotel.room where room_type = ?",nativeQuery = true)
    Integer getRoomByType(String roomType);

    @Query(value = "select count(r.room_type) from booking b join booking_total_rooms btr on b.booking_id = btr.booking_booking_id\n" +
            "join room r on btr.total_rooms_room_number = r.room_number \n" +
            "where cast(start_date as date) = ?1 and r.room_type = ?2",nativeQuery = true)
    Integer getRoomNotAvailable(String startDate,String roomType);

}
