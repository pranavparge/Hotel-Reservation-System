package com.hotel.payment.models.payment_details;

import java.util.Map;

import com.hotel.payment.models.CardPaymentModel;
import com.hotel.payment.models.Payment;

public class CardPaymentDetails implements PaymentDetails {
    private String cardNumber;
    private String expiry;
    private String cvv;
    private String cardName;

    public CardPaymentDetails(Map<String, Object> data){
        this.cardNumber = data.get("cardNumber").toString();
        this.cardName = data.get("cardName").toString();
        this.cvv = data.get("cvv").toString();
        this.expiry = data.get("expiry").toString();
    }

    @Override
    public void showPaymentDetails(){
        System.out.println("My Card details");
    }

    @Override
    public Payment createPaymentModel(double amount, String bookingID, String customerEmail){
        CardPaymentModel model = new CardPaymentModel(bookingID, customerEmail, amount, cardNumber);
        return model;
    }

    public void findCardServiceProvider(){
        System.out.println("Card service provider");
    }

    public String getCardNumber(){
        return this.cardNumber;
    }

    public String getCardName(){
        return this.cardName;
    }

    public String getExpiry(){
        return this.expiry;
    }

    public String getCvv(){
        return this.cvv;
    }

    @Override
    public String validate() {
        if(cardName == "" || cardNumber == "" || cvv == "" || expiry == ""){
            return "Incorrect card details for the payment";
        }
        return "";
    }

    
}
