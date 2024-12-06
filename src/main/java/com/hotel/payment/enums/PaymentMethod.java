package com.hotel.payment.enums;

public enum PaymentMethod {
    CARD,
    PAYPAL,
    DEFAULT;

    public String methodResponse(){
        switch(this){
            case CARD:
                return "Card";
            case DEFAULT:
                return "Default";
            case PAYPAL:
                return "Paypal";
            default:
                return "";

        }
    }
}
