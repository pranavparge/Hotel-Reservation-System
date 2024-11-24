package com.hotel.booking.entity;

import java.util.List;

public abstract class AdditionalServiceDecorator implements AdditionalServicesComponent {

    protected AdditionalServicesComponent component;

    public AdditionalServiceDecorator(AdditionalServicesComponent component) {
        this.component = component;
    }

    @Override
    public double calculateTotalPrice() {
        return component.calculateTotalPrice();
    }

    @Override
    public List<String> additionalServices() {
        return component.additionalServices();
    }
}
