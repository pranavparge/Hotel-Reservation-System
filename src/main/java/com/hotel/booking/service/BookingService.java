package com.hotel.booking.service;

import com.hotel.dto.request.BookingUpdateRequest;
import com.hotel.enums.ProgramType;
import com.hotel.enums.RoomType;
import com.hotel.repository.RoomRepository;
import com.hotel.room.entity.Room;
import com.hotel.user.entity.Customer;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.hotel.booking.entity.*;

import com.hotel.repository.BookingRepository;
import com.hotel.repository.CustomerRepository;

import com.hotel.dto.request.BookingCreateRequest;
import com.hotel.dto.response.BookingCreateResponse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService implements IBookingService {
    private final CustomerRepository customerRepository;
    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;

    private boolean isRoomAvailableForDates(Room room, Date startDate, Date endDate, Long bookingId) {
        // Check if the room is already booked for the given date range
        List<Booking> overlappingBookings = bookingRepository.findBookingsByRoomAndDates(room.getRoomNumber(), startDate, endDate);

        // Exclude the current booking being updated (if provided)
        if (bookingId != null) {
            overlappingBookings = overlappingBookings.stream()
                    .filter(booking -> !booking.getBookingId().equals(bookingId))
                    .collect(Collectors.toList());
        }

        return overlappingBookings.isEmpty();
    }


    @Override
    @Transactional
    public BookingCreateResponse createBooking(BookingCreateRequest request) {
        if (!request.getEndDate().after(request.getStartDate())) {
            throw new IllegalArgumentException("End date must be after start date and cannot be the same as start date.");
        }

        Customer customer = customerRepository.findById(request.getCustomerID())
                .orElseThrow(() -> new IllegalArgumentException("Customer with ID " + request.getCustomerID() + " does not exist."));

        List<Room> allocatedRooms = request.getRoomTypeToQuantity().entrySet().stream()
                .flatMap(entry -> {
                    RoomType roomType = entry.getKey();
                    int quantity = entry.getValue();

                    List<Room> availableRooms = roomRepository.findAvailableRoomsByTypeAndDates(
                            roomType, request.getStartDate(), request.getEndDate());

                    System.out.println("AVAILABLE ROOMS:" + availableRooms);
                    if (availableRooms.size() < quantity) {
                        throw new IllegalArgumentException("Not enough " + roomType + " rooms available. Requested: " + quantity
                                + ", Available: " + availableRooms.size());
                    }

                    return availableRooms.stream().limit(quantity);
                })
                .peek(room -> {
                    roomRepository.save(room);
                })
                .collect(Collectors.toList());

        int totalRoomCapacity = allocatedRooms.stream()
                .mapToInt(Room::getRoomCapacity)
                .sum();

        if (request.getTotalNumberOfGuests() > totalRoomCapacity) {
            throw new IllegalArgumentException("Total number of guests exceeds the room capacity. " +
                    "Room capacity: " + totalRoomCapacity + ", Guests: " + request.getTotalNumberOfGuests());
        }

        double roomsPrice = allocatedRooms.stream()
                .mapToDouble(Room::getRoomPrice)
                .sum();

        Booking booking = new Booking();
        booking.setCustomerID(request.getCustomerID());
        booking.setTotalRooms(allocatedRooms);
        booking.setStartDate(request.getStartDate());
        booking.setEndDate(request.getEndDate());
        booking.setTotalNumberOfGuests(request.getTotalNumberOfGuests());

        AdditionalServicesComponent decoratedBooking = booking;

        if (customer.getProgramType().equals(ProgramType.MEMBER)) {
            decoratedBooking = new BreakfastServiceDecorator(decoratedBooking);
            booking.addService("Breakfast");

            decoratedBooking = new LunchServiceDecorator(decoratedBooking);
            booking.addService("Lunch");

            decoratedBooking = new DinnerServiceDecorator(decoratedBooking);
            booking.addService("Dinner");
        } else {
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
        }

        double totalPrice = roomsPrice + decoratedBooking.getCost();

        double discount = 0.0;
        if (customer.getProgramType().equals(ProgramType.MEMBER)) {
            discount = 0.10;
            totalPrice = roomsPrice;
        } else if (customer.getProgramType().equals(ProgramType.LOYALTY)) {
            long bookingCount = bookingRepository.countByCustomerID(request.getCustomerID());
            if (bookingCount >= 10) {
                discount = 0.10;
            } else if (bookingCount >= 5) {
                discount = 0.05;
            }
        }

        totalPrice = totalPrice - (totalPrice * discount);
        booking.setTotalPrice(totalPrice);
        booking.setDiscount(discount);

        Booking savedBooking = bookingRepository.save(booking);
        return savedBooking.getBookingResponse();
    }

    @Override
    public List<BookingCreateResponse> viewBookings() {
        return bookingRepository.findAll()
                .stream()
                .map(Booking::getBookingResponse)
                .collect(Collectors.toList());
    }

    @Override
    public BookingCreateResponse viewBooking(Long bookingID) {
        return bookingRepository.findById(bookingID)
                .map(Booking::getBookingResponse)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with ID: " + bookingID));
    }

    @Override
    @Transactional
    public BookingCreateResponse updateBooking(Long bookingId, BookingUpdateRequest request) {
        if (!request.getEndDate().after(request.getStartDate())) {
            throw new IllegalArgumentException("End date must be after start date and cannot be the same as start date.");
        }

        return bookingRepository.findById(bookingId).map(existingBooking -> {
            // Validate and keep currently allocated rooms
            List<Room> existingRooms = existingBooking.getTotalRooms().stream()
                    .filter(room -> isRoomAvailableForDates(room, request.getStartDate(), request.getEndDate(), existingBooking.getBookingId()))
                    .collect(Collectors.toList());

            // Handle new room allocations if requested
            List<Room> newlyAllocatedRooms = request.getRoomTypeToQuantity().entrySet().stream()
                    .flatMap(entry -> {
                        RoomType roomType = entry.getKey();
                        int quantity = entry.getValue();

                        List<Room> availableRooms = roomRepository.findAvailableRoomsByTypeAndDates(
                                roomType, request.getStartDate(), request.getEndDate(), bookingId);

                        if (availableRooms.size() < quantity) {
                            throw new IllegalArgumentException("Not enough " + roomType + " rooms available. Requested: " + quantity
                                    + ", Available: " + availableRooms.size());
                        }

                        return availableRooms.stream().limit(quantity);
                    })
                    .collect(Collectors.toList());

            // Combine existing rooms and newly allocated rooms
            List<Room> updatedRooms = new ArrayList<>(existingRooms);
            updatedRooms.addAll(newlyAllocatedRooms);

            // Validate total guest capacity
            int totalRoomCapacity = updatedRooms.stream()
                    .mapToInt(Room::getRoomCapacity)
                    .sum();
            if (request.getTotalNumberOfGuests() > totalRoomCapacity) {
                throw new IllegalArgumentException("Total number of guests exceeds the room capacity. " +
                        "Room capacity: " + totalRoomCapacity + ", Guests: " + request.getTotalNumberOfGuests());
            }

            double roomsPrice = updatedRooms.stream()
                    .mapToDouble(Room::getRoomPrice)
                    .sum();

            existingBooking.setStartDate(request.getStartDate());
            existingBooking.setEndDate(request.getEndDate());
            existingBooking.setTotalNumberOfGuests(request.getTotalNumberOfGuests());
            existingBooking.getTotalRooms().clear();
            existingBooking.getTotalRooms().addAll(updatedRooms);

            // Handle additional services, discounts, etc.
            Customer customer = customerRepository.findById(existingBooking.getCustomerID())
                    .orElseThrow(() -> new IllegalArgumentException("Customer not found for ID: " + existingBooking.getCustomerID()));

            AdditionalServicesComponent decoratedBooking = existingBooking;
            existingBooking.getAdditionalServices().clear();

            if (customer.getProgramType().equals(ProgramType.MEMBER)) {
                decoratedBooking = new BreakfastServiceDecorator(decoratedBooking);
                existingBooking.addService("Breakfast");

                decoratedBooking = new LunchServiceDecorator(decoratedBooking);
                existingBooking.addService("Lunch");

                decoratedBooking = new DinnerServiceDecorator(decoratedBooking);
                existingBooking.addService("Dinner");
            } else {
                if (request.getAdditionalServices().contains("Breakfast")) {
                    decoratedBooking = new BreakfastServiceDecorator(decoratedBooking);
                    existingBooking.addService("Breakfast");
                }
                if (request.getAdditionalServices().contains("Lunch")) {
                    decoratedBooking = new LunchServiceDecorator(decoratedBooking);
                    existingBooking.addService("Lunch");
                }
                if (request.getAdditionalServices().contains("Dinner")) {
                    decoratedBooking = new DinnerServiceDecorator(decoratedBooking);
                    existingBooking.addService("Dinner");
                }
            }

            double totalPrice = roomsPrice;
            if (!customer.getProgramType().equals(ProgramType.MEMBER)) {
                totalPrice += decoratedBooking.getCost();
            }

            double discount = 0.0;
            if (customer.getProgramType().equals(ProgramType.MEMBER)) {
                discount = 0.10;
            } else if (customer.getProgramType().equals(ProgramType.LOYALTY)) {
                long bookingCount = bookingRepository.countByCustomerID(existingBooking.getCustomerID());
                if (bookingCount >= 10) {
                    discount = 0.10;
                } else if (bookingCount >= 5) {
                    discount = 0.05;
                }
            }

            totalPrice = totalPrice - (totalPrice * discount);
            existingBooking.setTotalPrice(totalPrice);
            existingBooking.setDiscount(discount);

            Booking updatedBooking = bookingRepository.save(existingBooking);

            return updatedBooking.getBookingResponse();
        }).orElseThrow(() -> new IllegalArgumentException("Booking not found for ID: " + bookingId));
    }

    @Override
    @Transactional
    public boolean deleteBooking(Long bookingId) {
        return bookingRepository.findById(bookingId).map(booking -> {
            booking.getTotalRooms().forEach(room -> {
                roomRepository.save(room);
            });

            booking.getTotalRooms().clear();
            booking.getAdditionalServices().clear();
            bookingRepository.saveAndFlush(booking);
            bookingRepository.delete(booking);
            return true;
        }).orElse(false);
    }
}
