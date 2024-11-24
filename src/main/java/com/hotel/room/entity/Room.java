package com.hotel.room.entity;

import lombok.Data;

import jakarta.persistence.*;

import com.hotel.enums.RoomType;
import com.hotel.enums.RoomStatus;
import com.hotel.dto.response.RoomCreateResponse;

@Data
@Entity
public class Room {
    @Id
    private String roomNumber;
    private int roomCapacity;
    private RoomStatus roomStatus;
    @Embedded
    private Price roomPrice;

    public Room() {}

    public Room(String roomNumber, int roomCapacity, Price roomPrice, RoomStatus roomStatus) {
        this.roomNumber = roomNumber;
        this.roomCapacity = roomCapacity;
        this.roomPrice = roomPrice;
        this.roomStatus = roomStatus;
    }

    public RoomType getRoomType() {
        return roomPrice.getRoomType();
    }

    public RoomCreateResponse getRoomResponse() {
        RoomCreateResponse response = new RoomCreateResponse();
        response.setRoomNumber(getRoomNumber());
        response.setRoomCapacity(getRoomCapacity());
        response.setRoomStatus(getRoomStatus());
        response.setRoomType(getRoomType());
        response.setRoomPrice(roomPrice.getPrice());
        return response;
    }
}
