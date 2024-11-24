package com.hotel.dto.request;

import java.util.Map;

import com.hotel.enums.PaymentMethod;
import com.hotel.payment.models.payment_details.CardPaymentDetails;
import com.hotel.payment.models.payment_details.PaymentDetails;
import com.hotel.payment.models.payment_details.PaypalPaymentDetails;


public class PaymentRequest {
    private String bookingID;
    private double amount;
    private PaymentMethod paymentMethod;
    private PaymentDetails paymentDetails;

    private PaymentDetails setPaymentDetails(Map<String, Object> data, PaymentMethod method) {
        switch (method) {
            case CARD:
                return new CardPaymentDetails(data); // Constructor for CardPaymentDetails
            case PAYPAL:
                return new PaypalPaymentDetails(data); // Constructor for PaypalPaymentDetails
            default:
                throw new IllegalArgumentException("Unsupported payment method: " + method);
        }
    }
    private PaymentMethod setPaymentMethod(String method){
        switch(method){
            case "Card":
                return PaymentMethod.CARD;
            case "Paypal":
                return PaymentMethod.PAYPAL;
            default:
                return PaymentMethod.CARD;        
        }
    }

    @SuppressWarnings("unchecked")
    public PaymentRequest(Map<String, Object> data){
        this.bookingID = data.get("bookingID").toString();
        this.amount = Double.parseDouble(data.get("amount").toString());
        this.paymentMethod = setPaymentMethod(data.get("paymentMethod").toString());
        this.paymentDetails = setPaymentDetails((Map<String, Object>) data.get("details") , this.paymentMethod);
    }

    public double getAmount(){
        return this.amount;
    }

    public String bookingID(){
        return this.bookingID;
    }

    public PaymentDetails getPaymentDetails(){
        return this.paymentDetails;
    }
}

