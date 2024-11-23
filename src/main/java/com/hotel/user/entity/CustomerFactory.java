package com.hotel.user.entity;

import com.hotel.enums.CustomerType;

public class CustomerFactory {
    public static Customer createCustomer(CustomerType customerType, String name, String email, String password) {
        return switch (customerType) {
            case MEMBER -> new Member(name, email, password);
            case LOYALTY -> new Loyalty(name, email, password);
            default -> throw new IllegalArgumentException("Invalid customer type: " + customerType);
        };
    }
}
