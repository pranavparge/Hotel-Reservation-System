package com.hotel.dto.response;

import com.hotel.enums.PaymentStatus;

import lombok.Data;

@Data
public class PaymentResponse {
    private String paymentID;
    private String bookingID;
    private double amount;
    private boolean result;

    public PaymentResponse(String paymentID, String bookingID, double amount, boolean result){
        this.bookingID = bookingID;
        this.paymentID = paymentID;
        this.amount = amount;
        this.result = result;
    }
}
