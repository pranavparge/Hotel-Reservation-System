package com.hotel.user.entity;

import jakarta.persistence.*;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.hotel.enums.CustomerType;
import com.hotel.booking.entity.Booking;
import com.hotel.dto.response.CustomerSignUpResponse;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public abstract class Customer extends User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerID;
    private CustomerType customerType;

    public Customer() {}

    public Customer(CustomerType customerType, String name, String email, String password) {
        super(name, email, password);
        this.customerType = customerType;
    }

    public abstract Booking createBooking();

    public CustomerSignUpResponse getCustomerResponse() {
        CustomerSignUpResponse response = new CustomerSignUpResponse();
        response.setCustomerID(customerID);
        response.setName(getName());
        response.setEmail(getEmail());
        return response;
    }
}
