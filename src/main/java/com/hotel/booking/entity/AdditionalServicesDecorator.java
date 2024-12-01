package com.hotel.booking.entity;

public abstract class AdditionalServicesDecorator implements AdditionalServicesComponent {
    protected AdditionalServicesComponent additionalServices;

    public AdditionalServicesDecorator(AdditionalServicesComponent additionalServices) {
        this.additionalServices = additionalServices;
    }
    @Override
    public double getCost() {
        return additionalServices.getCost();
    }
}
