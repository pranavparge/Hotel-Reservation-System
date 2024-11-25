package com.hotel.booking.entity;

// import javax.persistence.*;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.List;

@Entity
public class AdditionService {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    private String serviceType;

    private Float price;

    public AdditionService() {
    }

    public AdditionService(String serviceType, Float price) {
        this.serviceType = serviceType;
        this.price = price;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "AdditionalServices{" +
                "id=" + id +
                ", serviceType='" + serviceType + '\'' +
                ", price=" + price +
                '}';
    }
}
