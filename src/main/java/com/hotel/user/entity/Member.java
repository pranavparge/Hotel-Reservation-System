package com.hotel.user.entity;

import com.hotel.booking.entity.Booking;
import jakarta.persistence.Entity;
import com.hotel.enums.CustomerType;

@Entity
public class Member extends Customer {
    public Member(String name, String email, String password) {
        super(CustomerType.MEMBER, name, email, password);
    }

    public Member() {}

    @Override
    public Booking createBooking() {
        return null;
    }
}
