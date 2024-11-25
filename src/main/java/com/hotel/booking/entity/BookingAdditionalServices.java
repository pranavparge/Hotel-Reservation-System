package com.hotel.booking.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class BookingAdditionalServices {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    private String servicesType;

    private Float servicePrice;

    public BookingAdditionalServices() {
    }

    public BookingAdditionalServices(String servicesType, Float servicePrice) {
        this.servicesType = servicesType;
        this.servicePrice = servicePrice;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getServicesType() {
        return servicesType;
    }

    public void setServicesType(String servicesType) {
        this.servicesType = servicesType;
    }

    public Float getServicePrice() {
        return servicePrice;
    }

    public void setServicePrice(Float servicePrice) {
        this.servicePrice = servicePrice;
    }

    @Override
    public String toString() {
        return "BookingAdditionalServices{" +
                "id=" + id +
                ", servicesType='" + servicesType + '\'' +
                ", servicePrice=" + servicePrice +
                '}';
    }
}
