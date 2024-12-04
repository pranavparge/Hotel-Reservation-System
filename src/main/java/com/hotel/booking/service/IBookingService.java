package com.hotel.booking.service;

import com.hotel.dto.request.BookingCreateRequest;
import com.hotel.dto.request.BookingUpdateRequest;
import com.hotel.dto.response.BookingCreateResponse;

import java.util.List;

public interface IBookingService {
    BookingCreateResponse createBooking(BookingCreateRequest request);
    List<BookingCreateResponse> viewBookings();
    BookingCreateResponse viewBooking(Long bookingID);
    BookingCreateResponse updateBooking(Long bookingID, BookingUpdateRequest request);
    boolean deleteBooking(Long bookingId);
}
