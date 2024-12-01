package com.hotel.dto.response;

import com.hotel.enums.PaymentStatus;

import lombok.Data;

public class PaymentResponse {
    private String paymentID;
    private String bookingID;
    private PaymentStatus status;
    private double amount;
    private String customerEmail;

    private PaymentStatus setPaymentStatus(boolean result){
        if(result){
            return PaymentStatus.DONE;
        }
        return PaymentStatus.FAILURE;
    }

    public PaymentResponse(String paymentID, String bookingID, double amount, String customerEmail, boolean result){
        this.bookingID = bookingID;
        this.paymentID = paymentID;
        this.amount = amount;
        this.customerEmail = customerEmail;
        this.status = setPaymentStatus(result);
    }

    public PaymentStatus getPaymentStatus(){
        return this.status;
    }
}
