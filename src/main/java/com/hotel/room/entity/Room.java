package com.hotel.room.entity;

import lombok.Data;

import jakarta.persistence.*;

import com.hotel.enums.RoomType;
import com.hotel.dto.response.RoomCreateResponse;

@Data
@Entity
public class Room {
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
}
