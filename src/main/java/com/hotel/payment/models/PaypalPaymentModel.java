package com.hotel.payment.models;

import com.hotel.dto.response.PaymentResponse;
import com.hotel.payment.repository.PaymentRepositoryImpl;

public class PaypalPaymentModel extends PaymentModel{
    private String paypalID;

    public PaypalPaymentModel(double amount, String bookingID, String customerEmail, String paypalID){
        super(bookingID, amount, customerEmail);
        this.paypalID = paypalID;
    }

    @Override
    public boolean processPayment(){
        System.out.println("Payment processed via paypal"); 
        return true;
    }
}
