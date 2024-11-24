package com.hotel.booking.entity;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class LunchServiceDecorator extends AdditionalServiceDecorator{

    public LunchServiceDecorator(AdditionalServicesComponent component) {
        super(component);
    }

    @Override
    public double calculateTotalPrice() {
        return super.calculateTotalPrice() + 15.0;
    }

    @Override
    public List<String> additionalServices() {
        List<String> services = super.additionalServices();
        services.add("Lunch");
        return services;
    }

}
