package com.hotel.room.entity;

import lombok.Data;

import jakarta.persistence.*;

import com.hotel.enums.RoomType;
import com.hotel.dto.response.RoomCreateResponse;

@Data
@Entity
public class Room implements IRoomVisitor {
    @Id
    private String roomNumber;
    private int roomCapacity;
    @Embedded
    private Price roomPrice;
    @Transient
    private double flatRoomPrice;
    @Transient
    private RoomType flatRoomType;

    public Room() {}

    public Room(String roomNumber, int roomCapacity, Price roomPrice) {
        this.roomNumber = roomNumber;
        this.roomCapacity = roomCapacity;
        this.roomPrice = roomPrice;
    }

    public RoomType getRoomType() {
        return roomPrice != null ? roomPrice.getRoomType() : flatRoomType;
    }

    public double getRoomPrice() {
        return roomPrice != null ? roomPrice.getPrice() : flatRoomPrice;
    }

    public RoomCreateResponse getRoomResponse() {
        RoomCreateResponse response = new RoomCreateResponse();
        response.setRoomNumber(getRoomNumber());
        response.setRoomCapacity(getRoomCapacity());
        response.setRoomType(getRoomType());
        response.setRoomPrice(getRoomPrice());
        return response;
    }

    public void setRoomPrice(double flatRoomPrice) {
        this.flatRoomPrice = flatRoomPrice;
        if (flatRoomType != null) {
            this.roomPrice = new Price(flatRoomPrice, flatRoomType);
        }
    }

    public void setRoomType(RoomType flatRoomType) {
        this.flatRoomType = flatRoomType;
        if (flatRoomPrice > 0) {
            this.roomPrice = new Price(flatRoomPrice, flatRoomType);
        }
    }

    @Transient
    private double finalPrice;

    @Override
    public void visit(SingleRoomPrice singleRoomPrice, double totalRoom, double roomBooked) {
        finalPrice = singleRoomPrice.getPrice();
        adjustPriceBasedOnAvailability(totalRoom,roomBooked);
    }

    @Override
    public void visit(DoubleRoomPrice doubleRoomPrice, double totalRoom, double roomBooked) {
        finalPrice = doubleRoomPrice.getPrice();
        adjustPriceBasedOnAvailability(totalRoom,roomBooked);
    }

    @Override
    public void visit(SuiteRoomPrice suiteRoomPrice, double totalRoom, double roomBooked) {
        finalPrice = suiteRoomPrice.getPrice();
        adjustPriceBasedOnAvailability(totalRoom,roomBooked);
    }

    private void adjustPriceBasedOnAvailability(double totalRoom, double roomBooked) {
        double availabilityPercentage = ((totalRoom - roomBooked) / (double) totalRoom) * 100;

        // Adjust pricing based on availability percentage
        if (availabilityPercentage <= 10) { // Low availability: less than 10%
            finalPrice *= 1.5; // 50% increase
        } else if (availabilityPercentage <= 50) { // Medium availability: 10% - 50%
            finalPrice *= 1.2; // 20% increase
        } else if (availabilityPercentage > 50) { // High availability: more than 50%
            finalPrice *= 1.0; // Base Price
        }
    }

    public double getFinalPrice() {
        return finalPrice;
    }
}
