package com.hotel.payment.entity;

import org.springframework.stereotype.Component;

@Component
public class SmsServiceObserver implements Observer{
    @Override
    public void update(Payment paymentModel, String message) {
        System.out.println("Payment SMS sent to the customer: " + paymentModel.customerEmail + " For payment mode: "+paymentModel.paymentMethod.name());
    }
}