package com.hotel.booking.entity;

public class BreakfastServiceDecorator extends AdditionalServicesDecorator {
    public BreakfastServiceDecorator(AdditionalServicesComponent additionalServicesComponent) {
        super(additionalServicesComponent);
    }
    @Override
    public double getCost() {
        return super.getCost() + 5.0;
    }
}
