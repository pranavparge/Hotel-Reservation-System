package com.hotel.payment.models;

import com.hotel.dto.response.PaymentResponse;
import com.hotel.enums.PaymentStatus;
// import com.hotel.payment.repository.PaymentRepositoryImpl;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("PAYPAL")
public class PaypalPaymentModel extends PaymentModel{
    private String paypalID;

    public PaypalPaymentModel(double amount, String bookingID, String customerEmail, String paypalID){
        super(bookingID, amount, customerEmail);
        this.paypalID = paypalID;
    }

    @Override
    public void processPayment(){
        System.out.println("Payment processed via paypal"); 
        this.status = PaymentStatus.DONE;
    }

    @Override
    public boolean refundPayment() {
        System.out.println("Payment refunded for paypal payment");
        return true;
    }
}
