package com.hotel.booking.controller;

import com.hotel.dto.request.BookingUpdateRequest;
import com.hotel.repository.BookingRepository;
import com.hotel.repository.RoomRepository;
import com.hotel.room.entity.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import com.hotel.booking.service.IBookingService;
import com.hotel.dto.request.BookingCreateRequest;
import com.hotel.dto.response.BookingCreateResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookingController {
    private final IBookingService bookingService;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    BookingRepository bookingRepository;

    @PostMapping("/customer/bookings/create")
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

    @GetMapping("/staff/bookings")
    public ResponseEntity<?> viewBookings(@RequestParam(value = "bookingID", required = false) Long bookingID) {
        try {
            if (bookingID != null) {
                BookingCreateResponse response = bookingService.viewBooking(bookingID);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                List<BookingCreateResponse> response = bookingService.viewBookings();
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Booking not found: " + e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Unable to fetch bookings!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/staff/bookings/update")
    public ResponseEntity<?> updateBooking(@RequestParam(value = "bookingID") Long bookingID, @Valid @RequestBody BookingUpdateRequest updatedRequest) {
        try {
            BookingCreateResponse updatedBooking = bookingService.updateBooking(bookingID, updatedRequest);
            return new ResponseEntity<>(updatedBooking, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Invalid booking update request: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Unable to update booking!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/staff/bookings/delete")
    public ResponseEntity<?> deleteBooking(@RequestParam(value = "bookingID") Long bookingID) {
        try {
            boolean isDeleted = bookingService.deleteBooking(bookingID);
            if (isDeleted) {
                return new ResponseEntity<>("Booking deleted successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Booking not found with ID: " + bookingID, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Unable to delete booking!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/customer/bookings/price")
    public ResponseEntity<?> getRoomPrice(@RequestParam(value = "startDate") String startDate) {

        try {
            String roomPrice = bookingService.getRoomPrice(startDate);
            if (roomPrice != null) {
                return new ResponseEntity<>(roomPrice, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Price not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Unable to find price!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
