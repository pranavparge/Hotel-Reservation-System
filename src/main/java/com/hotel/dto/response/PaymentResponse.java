package com.hotel.dto.response;

import com.hotel.enums.PaymentStatus;

import lombok.Data;

public class PaymentResponse {
    private String paymentID;
    private String bookingID;
    private PaymentStatus status;

    private PaymentStatus setPaymentStatus(boolean result){
        if(result){
            return PaymentStatus.DONE;
        }
        return PaymentStatus.FAILURE;
    }

    public PaymentResponse(String paymentID, String bookingID, boolean result){
        this.bookingID = bookingID;
        this.paymentID = paymentID;
        this.status = setPaymentStatus(result);
    }
}
