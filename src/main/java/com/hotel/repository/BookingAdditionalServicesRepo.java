package com.hotel.repository;

import com.hotel.booking.entity.BookingAdditionalServices;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingAdditionalServicesRepo extends JpaRepository<BookingAdditionalServices,Integer> {
}
