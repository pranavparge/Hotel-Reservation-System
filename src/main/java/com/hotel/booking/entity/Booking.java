package com.hotel.booking.entity;

import lombok.Data;

import java.util.Date;
import java.util.List;
import java.time.Instant;
import java.util.ArrayList;

import jakarta.persistence.*;

import com.hotel.room.entity.Room;
import com.hotel.dto.response.BookingCreateResponse;

import org.hibernate.annotations.CreationTimestamp;

@Data
@Entity
public class Booking implements AdditionalServicesComponent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;
    @CreationTimestamp
    private Instant timeStamp;
    private Long customerID;
    @OneToMany
    private List<Room> totalRooms;
    private Date startDate;
    private Date endDate;
    private int totalNumberOfGuests;
    // Here we look in totalRooms and calculate the price using roomPrice of Room object
    private double basePrice;
    // Here we look in customerID and check in Customer table the customerType
    private double discount;
    @ElementCollection
    private List<String> additionalServices = new ArrayList<>();

    // Here the base cost will be the price of all the rooms added
    @Override
    public double getCost() {
        return 0;
    }
    public void addService(String serviceName) {
        additionalServices.add(serviceName);
    }
    public BookingCreateResponse getBookingResponse() {
        BookingCreateResponse response = new BookingCreateResponse();
        response.setBookingID(bookingId);
        response.setCustomerID(customerID);
        response.setAdditionalServices(additionalServices);
        response.setBasePrice(basePrice);
        response.setStartDate(startDate);
        response.setEndDate(endDate);
        response.setTotalNumberOfGuests(totalNumberOfGuests);
        response.setTimeStamp(Date.from(timeStamp));
        return response;
    }
}
