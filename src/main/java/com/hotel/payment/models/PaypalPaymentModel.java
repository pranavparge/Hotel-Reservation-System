package com.hotel.payment.models;

import com.hotel.dto.response.PaymentResponse;
import com.hotel.payment.repository.PaymentRepositoryImpl;

public class PaypalPaymentModel extends PaymentModel{
    private String paypalID;

    public PaypalPaymentModel(double amount, String bookingID, String paypalID){
        super(bookingID, amount);
        this.paypalID = paypalID;
    }

    @Override
    public PaymentResponse processPayment(){
        System.out.println("Payment processed via paypal"); 
        PaymentRepositoryImpl repositoryImpl = new PaymentRepositoryImpl();
        final boolean result =  repositoryImpl.processPaypalPayment(this.amount, this.paypalID);

        return new PaymentResponse("1", this.bookingID, result);
    }
}
