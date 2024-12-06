package com.hotel.payment.factory;

import org.springframework.stereotype.Component;

import com.hotel.payment.models.Payment;
import com.hotel.payment.models.payment_details.PaymentDetails;

@Component
public class PaymentFactory {
    public Payment createPayment(double amount, String bookingID, String customerEmail, PaymentDetails details){
        return details.createPaymentModel(amount, bookingID, customerEmail);
    }
}
