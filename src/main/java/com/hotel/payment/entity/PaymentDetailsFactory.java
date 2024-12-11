package com.hotel.payment.entity;

import java.util.Map;

import com.hotel.enums.PaymentMethod;
import org.springframework.stereotype.Component;

@Component
public class PaymentDetailsFactory {
    public PaymentMethod getPaymentMethod(String method){
        try {
            return switch (method) {
                case "Card" -> PaymentMethod.CARD;
                case "Paypal" -> PaymentMethod.PAYPAL;
                default -> PaymentMethod.DEFAULT;
            };
        } catch (Exception e) {
            throw new RuntimeException("Invalid payment method");
        }

    }

    public IPaymentDetails getPaymentDetails(Map<String, Object> data, PaymentMethod method){
        try {
            return switch (method) {
                case CARD -> new CardPaymentDetails(data); // Constructor for CardPaymentDetails
                case PAYPAL -> new PaypalPaymentDetails(data); // Constructor for PaypalPaymentDetails
                default -> throw new IllegalArgumentException("Unsupported payment method: " + method);
            };
        } catch (Exception e) {
            throw new RuntimeException("Invalid payment details");
        }

    }
}
