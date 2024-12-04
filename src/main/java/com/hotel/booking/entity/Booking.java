package com.hotel.booking.entity;

import com.hotel.dto.response.RoomCreateResponse;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.time.Instant;
import java.util.ArrayList;
import java.util.stream.Collectors;

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
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(
            name = "booking_total_rooms",
            joinColumns = @JoinColumn(name = "booking_booking_id", referencedColumnName = "bookingId"),
            inverseJoinColumns = @JoinColumn(name = "total_rooms_room_number", referencedColumnName = "roomNumber", unique = false)
    )
    private List<Room> totalRooms = new ArrayList<>();
    private Date startDate;
    private Date endDate;
    private int totalNumberOfGuests;
    private double totalPrice;
    private double discount;
    @ElementCollection
    private List<String> additionalServices = new ArrayList<>();

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
        response.setTotalPrice(totalPrice);
        response.setStartDate(startDate);
        response.setEndDate(endDate);
        response.setTotalNumberOfGuests(totalNumberOfGuests);
        response.setTimeStamp(Date.from(timeStamp));
        List<RoomCreateResponse> roomResponses = totalRooms.stream()
                .map(room -> {
                    RoomCreateResponse roomResponse = new RoomCreateResponse();
                    roomResponse.setRoomNumber(room.getRoomNumber());
                    roomResponse.setRoomCapacity(room.getRoomCapacity());
                    roomResponse.setRoomType(room.getRoomType());
                    roomResponse.setRoomPrice(room.getRoomPrice());
                    return roomResponse;
                })
                .collect(Collectors.toList());
        response.setTotalRooms(roomResponses);
        return response;
    }
}
