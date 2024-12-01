package com.hotel.payment.models;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;

import com.hotel.dto.response.PaymentResponse;
import com.hotel.enums.PaymentMethod;
import com.hotel.enums.PaymentStatus;
import com.hotel.payment.email_service.subjects.PaymentNotifier;
import com.hotel.payment.models.payment_details.PaymentDetails;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;

// import com.hotel.dto.response.PaymentResponse;
// import com.hotel.enums.PaymentMethod;
// import com.hotel.enums.PaymentStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;

@Entity
@Inheritance()
@DiscriminatorColumn(name = "method")
public abstract class PaymentModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long paymentID;
    @CreationTimestamp
    private Instant timeStamp;
    @Enumerated(EnumType.STRING)
    @Column(name = "method", insertable = false, updatable = false)
    public PaymentMethod method;
    @Enumerated(EnumType.STRING)
    public PaymentStatus status;
    public String bookingID;
    public double amount;
    public String customerEmail;

    public PaymentModel(String bookingID, double amount, String customerEmail){
        this.bookingID = bookingID;
        this.amount = amount;
        this.customerEmail = customerEmail;
    }

    public abstract void processPayment();
    public abstract boolean refundPayment();

    public PaymentResponse paymentResponse() {
        PaymentResponse response = new PaymentResponse(paymentID.toString(), bookingID, amount, true);
        return response;
    }
    
    public String getBookingID(){
        return this.bookingID;
    }
    public Double getAmount(){
        return this.amount;
    }
    public String getcustomerEmail(){
        return this.customerEmail;
    }


    public void notifyCustomer(PaymentNotifier notifier, String message){
        //Observer logic here
        if(this.status == PaymentStatus.DONE){
            notifier.notifyObservers(this, message);
        }
    }
}


