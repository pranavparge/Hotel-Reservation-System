package com.hotel.booking.entity;

import java.util.Collections;
import java.util.List;

public class AdditionalServices implements AdditionalServicesComponent{
    @Override
    public double calculateTotalPrice() {
        return 0;
    }

    @Override
    public List<String> additionalServices() {
        return Collections.singletonList("Additional Services");
    }
}
