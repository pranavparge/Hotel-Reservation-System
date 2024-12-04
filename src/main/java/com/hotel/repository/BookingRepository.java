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
}
