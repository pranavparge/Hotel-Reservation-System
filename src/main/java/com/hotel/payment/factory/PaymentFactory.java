package com.hotel.payment.factory;

import org.springframework.stereotype.Component;

import com.hotel.enums.PaymentMethod;
import com.hotel.payment.models.CardPaymentModel;
import com.hotel.payment.models.PaymentModel;
import com.hotel.payment.models.PaypalPaymentModel;
import com.hotel.payment.models.payment_details.PaymentDetails;

@Component
public class PaymentFactory {
    public PaymentModel createPayment(double amount, String bookingID, String customerEmail, PaymentDetails details){
        return details.createPaymentModel(amount, bookingID, customerEmail);
    }
}
