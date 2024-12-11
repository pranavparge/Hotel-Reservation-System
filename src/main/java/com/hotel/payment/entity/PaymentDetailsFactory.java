package com.hotel.payment.entity;

import java.util.Map;

import com.hotel.enums.PaymentMethod;
import org.springframework.stereotype.Component;

@Component
public class PaymentDetailsFactory {
    public PaymentMethod getPaymentMethod(String method){
        return switch (method) {
            case "Card" -> PaymentMethod.CARD;
            case "Paypal" -> PaymentMethod.PAYPAL;
            default -> PaymentMethod.DEFAULT;
        };
    }

    public IPaymentDetails getPaymentDetails(Map<String, Object> data, PaymentMethod method){
        return switch (method) {
            case CARD -> new CardPaymentDetails(data); // Constructor for CardPaymentDetails
            case PAYPAL -> new PaypalPaymentDetails(data); // Constructor for PaypalPaymentDetails
            default -> throw new IllegalArgumentException("Unsupported payment method: " + method);
        };
    }
}
