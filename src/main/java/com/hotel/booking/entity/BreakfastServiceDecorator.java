package com.hotel.booking.entity;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class BreakfastServiceDecorator extends AdditionalServiceDecorator{

    public BreakfastServiceDecorator(AdditionalServicesComponent component) {
        super(component);
    }

    @Override
    public double calculateTotalPrice() {
        return super.calculateTotalPrice() + 10.0;
    }

    @Override
    public List<String> additionalServices() {
        List<String> services = super.additionalServices();
        services.add("Breakfast");
        return services;
    }
}
