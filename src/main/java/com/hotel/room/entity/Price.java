package com.hotel.room.entity;

import jakarta.persistence.*;
import com.hotel.enums.RoomType;

@Embeddable
public class Price implements IPrice {
    private double price;
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
}
