package com.hotel.payment.models;

import com.hotel.dto.response.PaymentResponse;
import com.hotel.payment.repository.PaymentRepositoryImpl;

public class CardPaymentModel extends PaymentModel{
    private String cardNumber;

    public CardPaymentModel(double amount, String bookingID, String cardNumber){
        super(bookingID, amount);
        this.cardNumber = cardNumber;
    }

    @Override
    public PaymentResponse processPayment(){
        System.out.println("Payment processed via credit card"); 
        PaymentRepositoryImpl repositoryImpl = new PaymentRepositoryImpl();
        final boolean result =  repositoryImpl.processCardPayment(this.amount, this.cardNumber);

        return new PaymentResponse("1", this.bookingID, result);
    }
}
