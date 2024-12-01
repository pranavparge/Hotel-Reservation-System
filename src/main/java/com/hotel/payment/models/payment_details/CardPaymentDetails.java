package com.hotel.payment.models.payment_details;

import java.util.Map;

import com.hotel.payment.models.CardPaymentModel;
import com.hotel.payment.models.PaymentModel;

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
    public PaymentModel createPaymentModel(double amount, String bookingID, String customerEmail){
        return new CardPaymentModel(amount, bookingID, customerEmail, this.cardNumber);
    }

    public void findCardServiceProvider(){
        System.out.println("Card service provider");
    }

    public String getCardNumber(){
        return this.cardNumber;
    }
}
