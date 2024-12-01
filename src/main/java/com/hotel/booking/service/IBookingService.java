package com.hotel.booking.service;

import com.hotel.dto.request.BookingCreateRequest;
import com.hotel.dto.response.BookingCreateResponse;

public interface IBookingService {
    BookingCreateResponse createBooking(BookingCreateRequest request);
}
