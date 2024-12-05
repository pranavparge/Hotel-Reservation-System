package com.hotel.repository;

import com.hotel.booking.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    long countByCustomerID(@Param("customerID") Long customerID);
    @Query("""
        SELECT b FROM Booking b 
        JOIN b.totalRooms r 
        WHERE r.roomNumber = :roomNumber 
        AND b.startDate < :endDate 
        AND b.endDate > :startDate
    """)
    List<Booking> findBookingsByRoomAndDates(
            @Param("roomNumber") String roomNumber,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate
    );

    @Query(value = "select count(r.room_type) from booking b join booking_total_rooms btr on b.booking_id = btr.booking_booking_id\n" +
            "join room r on btr.total_rooms_room_number = r.room_number \n" +
            "where cast(start_date as date) = ?1 and r.room_type = ?2",nativeQuery = true)
    Integer getBookingList(String startDate,String roomType);

}
