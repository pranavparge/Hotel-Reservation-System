package com.hotel.booking.entity;

public class DinnerServiceDecorator  extends AdditionalServicesDecorator {
    public DinnerServiceDecorator(IAdditionalServicesComponent IAdditionalServicesComponent) {
        super(IAdditionalServicesComponent);
    }
    @Override
    public double getCost() {
       return super.getCost() + 10.0;
    }
}
