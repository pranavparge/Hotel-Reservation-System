package com.hotel.room.entity;

import jakarta.persistence.*;
import com.hotel.enums.RoomType;

@Embeddable
public class Price implements IPrice {
    private double price;
    @Enumerated(EnumType.STRING)
    private RoomType roomType;

    public Price() {}

    protected Price(double price, RoomType roomType) {
        this.price = price;
        this.roomType = roomType;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public RoomType getRoomType() {
        return roomType;
    }

    public void accept(RoomVisitor visitor, double availabilityPercentage, Integer roomBooked) {}

}
