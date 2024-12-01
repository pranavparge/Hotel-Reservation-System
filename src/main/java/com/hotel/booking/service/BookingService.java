package com.hotel.booking.service;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.hotel.booking.entity.*;

import com.hotel.repository.BookingRepository;

import com.hotel.dto.request.BookingCreateRequest;
import com.hotel.dto.response.BookingCreateResponse;

@Service
@RequiredArgsConstructor
public class BookingService implements IBookingService {
    private final BookingRepository bookingRepository;

    @Override
    public BookingCreateResponse createBooking(BookingCreateRequest request) {
        Booking booking = new Booking();
        booking.setCustomerID(request.getCustomerID());
        booking.setTotalRooms(request.getTotalRooms());
        booking.setStartDate(request.getStartDate());
        booking.setEndDate(request.getEndDate());
        booking.setTotalNumberOfGuests(request.getTotalNumberOfGuests());

        AdditionalServicesComponent decoratedBooking = booking;

        if (request.isAddBreakfast()) {
            decoratedBooking = new BreakfastServiceDecorator(decoratedBooking);
            booking.addService("Breakfast");
        }
        if (request.isAddLunch()) {
            decoratedBooking = new LunchServiceDecorator(decoratedBooking);
            booking.addService("Lunch");
        }
        if (request.isAddDinner()) {
            decoratedBooking = new DinnerServiceDecorator(decoratedBooking);
            booking.addService("Dinner");
        }
        booking.setBasePrice(decoratedBooking.getCost());
        Booking savedBooking = bookingRepository.save(booking);
        return savedBooking.getBookingResponse();
    }
}
