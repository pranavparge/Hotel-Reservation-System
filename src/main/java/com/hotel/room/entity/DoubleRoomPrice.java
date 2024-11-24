package com.hotel.room.entity;

import com.hotel.enums.RoomType;
import jakarta.persistence.Embeddable;

@Embeddable
public class DoubleRoomPrice extends Price {
    public DoubleRoomPrice() {
        super(150.0, RoomType.DOUBLE);
    }
}
