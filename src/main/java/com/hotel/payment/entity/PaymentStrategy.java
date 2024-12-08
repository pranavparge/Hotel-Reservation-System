package com.hotel.payment.entity;

import org.springframework.stereotype.Component;

/// This strategy class manages the creation of dynamic [Payment]
/// [CardPaymentModel] or [PaypalPaymentModel] for bringing about the card or wallet payments.
/// This method helps abstract the management of the logic to verify the payment methodologies.
@Component
public class PaymentStrategy {
    public Payment createPayment(double amount, String bookingID, String customerEmail, IPaymentDetails details){
        return details.createPaymentModel(amount, bookingID, customerEmail);
    }
}
