package com.hotel.room.entity;

import com.hotel.enums.RoomType;

public class SingleRoomPrice implements Price {
    @Override
    public double getPrice() {
        return 100.0;
    }

    @Override
    public RoomType getRoomType() {
        return RoomType.SINGLE;
    }
}
