package com.hotel.payment.entity;

import java.util.Map;

public class PaypalPaymentDetails implements IPaymentDetails {
    private String paypalID;

    public PaypalPaymentDetails(Map<String, Object> data){
        this.paypalID = data.get("paypalID").toString();
    }

    @Override
    public Payment createPaymentModel(double amount, String bookingID, String customerEmail){
        PaypalPaymentModel model = new PaypalPaymentModel(bookingID, customerEmail, amount, paypalID);
        return model;
    }

    @Override
    public void showPaymentDetails(){
        System.out.println("Show paypal details");
    }

    public String getID(){
        return this.paypalID;
    }

    @Override
    public String validate() {
        if(paypalID == ""){
            return "Incorrect paypal details for the payment";
        }
        return "";
    }
}