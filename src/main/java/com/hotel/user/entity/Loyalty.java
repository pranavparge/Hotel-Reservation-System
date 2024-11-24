package com.hotel.user.entity;

import jakarta.persistence.Entity;

import com.hotel.enums.CustomerType;
import com.hotel.booking.entity.Booking;

@Entity
public class Loyalty extends Customer {
    public Loyalty(String name, String email, String password) {
        super(CustomerType.LOYALTY, name, email, password);
    }

    public Loyalty() {}

    @Override
    public Booking createBooking() {
        return null;
    }
}
