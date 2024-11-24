package com.hotel.payment.models;

import com.hotel.dto.response.PaymentResponse;
import com.hotel.payment.repository.PaymentRepositoryImpl;

public class CardPaymentModel extends PaymentModel{
    private String cardNumber;

    public CardPaymentModel(double amount, String bookingID, String customerEmail, String cardNumber){
        super(bookingID, amount, customerEmail);
        this.cardNumber = cardNumber;
    }

    @Override
    public boolean processPayment(){
        System.out.println("Payment processed via credit card"); 
        return true;
    }
}
