package com.hotel.payment.entity;

import java.util.Map;

/// Payment details implementing the payment details
/// Details correspond to the specifications of paypal wallet payment details that are required.
/// Helps create the corresponding [PaypalPaymentModel].
public class PaypalPaymentDetails implements IPaymentDetails {
    /// The paypal id required to make payments via [Paymethod.PAYPAL]
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
