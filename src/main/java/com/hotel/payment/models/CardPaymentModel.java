package com.hotel.payment.models;

import com.hotel.dto.response.PaymentResponse;
import com.hotel.enums.PaymentStatus;
// import com.hotel.payment.repository.PaymentRepositoryImpl;
import com.hotel.payment.models.payment_details.PaymentDetails;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("CARD")
public class CardPaymentModel extends PaymentModel{
    private String cardNumber;

    public CardPaymentModel(double amount, String bookingID, String customerEmail, String cardNumber){
        super(bookingID, amount, customerEmail);
        this.cardNumber = cardNumber;
        
    }

    @Override
    public void processPayment(){
        System.out.println("Payment processed via credit card"); 
        this.status = PaymentStatus.DONE;
    }

    @Override
    public boolean refundPayment() {
        System.out.println("Payment refunded for credit card payment");
        return true;
    }
}
