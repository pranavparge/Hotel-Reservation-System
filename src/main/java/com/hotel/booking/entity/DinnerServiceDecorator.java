package com.hotel.booking.entity;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class DinnerServiceDecorator  extends AdditionalServiceDecorator{

    public DinnerServiceDecorator(AdditionalServicesComponent component) {
        super(component);
    }

    @Override
    public double calculateTotalPrice() {
       return super.calculateTotalPrice() + 20.0;
    }

    @Override
    public List<String> additionalServices() {
        List<String> services = super.additionalServices();
        services.add("Dinner");
        return services;
    }


}
