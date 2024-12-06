package com.hotel.payment.entity;

import org.springframework.stereotype.Component;

@Component
public class PaymentFactory {
    public Payment createPayment(double amount, String bookingID, String customerEmail, IPaymentDetails details){
        return details.createPaymentModel(amount, bookingID, customerEmail);
    }
}
