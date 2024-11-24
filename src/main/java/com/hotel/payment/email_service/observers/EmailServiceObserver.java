package com.hotel.payment.email_service.observers;

import org.springframework.stereotype.Component;

import com.hotel.payment.models.PaymentModel;

public class EmailServiceObserver implements Observer{

    @Override
    public void update(PaymentModel paymentModel) {
        System.out.print("Payment Notification sent to the customer" + paymentModel.bookingID);
    }
    
}
