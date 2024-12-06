package com.hotel.booking.entity;

public abstract class AdditionalServicesDecorator implements IAdditionalServicesComponent {
    protected IAdditionalServicesComponent additionalServices;

    public AdditionalServicesDecorator(IAdditionalServicesComponent additionalServices) {
        this.additionalServices = additionalServices;
    }
    @Override
    public double getCost() {
        return additionalServices.getCost();
    }
}
