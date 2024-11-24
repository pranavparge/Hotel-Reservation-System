package com.hotel.payment.models.payment_details;

import java.util.Map;

import com.hotel.payment.models.PaymentModel;
import com.hotel.payment.models.PaypalPaymentModel;

public class PaypalPaymentDetails implements PaymentDetails{
    private String paypalID;

    public PaypalPaymentDetails(Map<String, Object> data){
        this.paypalID = data.get("paypalID").toString();
    }

    @Override
    public PaymentModel createPaymentModel(double amount, String bookingID, String customerEmail){
        return new PaypalPaymentModel(amount, bookingID, customerEmail, this.paypalID);
    }

    @Override
    public void showPaymentDetails(){
        System.out.println("Show paypal details");
    }

    public String getID(){
        return this.paypalID;
    }
}
