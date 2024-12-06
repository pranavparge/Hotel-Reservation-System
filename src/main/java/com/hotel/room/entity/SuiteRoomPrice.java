package com.hotel.room.entity;

import com.hotel.enums.RoomType;
import jakarta.persistence.Embeddable;

@Embeddable
public class SuiteRoomPrice extends Price {
    public SuiteRoomPrice() {
        super(200.0, RoomType.SUITE);
    }

    @Override
    public void accept(IRoomVisitor visitor, double totalRoom, Integer roomBooked) {
        visitor.visit(this, totalRoom, roomBooked);
    }

}
