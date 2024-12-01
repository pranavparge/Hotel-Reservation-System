package com.hotel.booking.entity;

public class DinnerServiceDecorator  extends AdditionalServicesDecorator {
    public DinnerServiceDecorator(AdditionalServicesComponent additionalServicesComponent) {
        super(additionalServicesComponent);
    }
    @Override
    public double getCost() {
       return super.getCost() + 10.0;
    }
}
