package com.hotel.payment.notification_service.email_service.observers;

import org.springframework.stereotype.Component;

import com.hotel.payment.models.Payment;

@Component
public class SmsServiceObserver implements Observer{
    @Override
    public void update(Payment paymentModel, String message) {
        System.out.println("Payment SMS sent to the customer: " + paymentModel.customerEmail + " For payment mode: "+paymentModel.paymentMethod.name());
    }
}