package com.hotel.booking.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hotel.booking.service.IBookingService;
import com.hotel.dto.request.BookingCreateRequest;
import com.hotel.dto.response.BookingCreateResponse;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final IBookingService bookingService;

    @PostMapping("/create")
    public ResponseEntity<?> createBooking(@Valid @RequestBody BookingCreateRequest request) {
        try {
            BookingCreateResponse newBooking = bookingService.createBooking(request);
            return new ResponseEntity<>(newBooking, HttpStatus.OK);
        } catch (IllegalArgumentException illegalArgumentException) {
            return new ResponseEntity<>("Invalid booking request: " + illegalArgumentException.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Booking not created!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
