package com.hotel.payment.enums;

public enum PaymentStatus {
    TRANSIT,
    DONE,
    FAILURE;

    public String paymentstatusResponse(){
        switch(this){
            case DONE:
                return "Payment successfull";
            case FAILURE:
                return "Payment Failure";
            case TRANSIT:
                return "Payment in Transit";
            default:
                return "";

        }
    }

    public String refundStatusResponse(){
        switch(this){
            case DONE:
                return "Refund processed successfully";
            case FAILURE:
                return "Refund Failure";
            case TRANSIT:
                return "Refund in Transit";
            default:
                return "";

        }
    }

    public boolean statusResult(){
        switch(this){
            case DONE:
                return true;
            case FAILURE:
                return false;
            case TRANSIT:
                return false;
            default:
                return false;

        }
    }
}
