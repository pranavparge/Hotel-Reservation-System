package com.hotel.booking.entity;

import java.util.List;

public interface AdditionalServicesComponent {

    double calculateTotalPrice();
    List<String> additionalServices();

}
