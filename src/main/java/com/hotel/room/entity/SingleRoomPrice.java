package com.hotel.room.entity;

import com.hotel.enums.RoomType;
import jakarta.persistence.Embeddable;

@Embeddable
public class SingleRoomPrice extends Price {
    public SingleRoomPrice() {
        super(100.0, RoomType.SINGLE);
    }

    @Override
    public void accept(IRoomVisitor visitor, double totalRoom, Integer roomBooked) {
        visitor.visit(this, totalRoom, roomBooked);
    }

}
