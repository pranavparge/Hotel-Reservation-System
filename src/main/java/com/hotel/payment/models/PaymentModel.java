package com.hotel.payment.models;

import com.hotel.dto.response.PaymentResponse;
import com.hotel.enums.PaymentMethod;
import com.hotel.enums.PaymentStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;

@Entity
public abstract class PaymentModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long paymentID;

    public String bookingID;
    public double amount;
    public String customerEmail;

    public PaymentModel(String bookingID, double amount, String customerEmail){
        this.bookingID = bookingID;
        this.amount = amount;
        this.customerEmail = customerEmail;
    }

    public abstract boolean processPayment();
    
    public String getBookingID(){
        return this.bookingID;
    }
    public Double getAmount(){
        return this.amount;
    }
    public String getcustomerEmail(){
        return this.customerEmail;
    }
}


