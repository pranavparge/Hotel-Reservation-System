package com.hotel.booking.entity;

public class BreakfastServiceDecorator extends AdditionalServicesDecorator {
    public BreakfastServiceDecorator(IAdditionalServicesComponent IAdditionalServicesComponent) {
        super(IAdditionalServicesComponent);
    }
    @Override
    public double getCost() {
        return super.getCost() + 5.0;
    }
}
