package com.hotel.booking.entity;

public class LunchServiceDecorator extends AdditionalServicesDecorator {
    public LunchServiceDecorator(AdditionalServicesComponent additionalServicesComponent) {
        super(additionalServicesComponent);
    }
    @Override
    public double getCost() {
        return super.getCost() + 10.0;
    }
}
