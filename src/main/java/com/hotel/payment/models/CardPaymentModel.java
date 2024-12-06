package com.hotel.payment.models;

import com.hotel.payment.dto.PaymentResponse;
import com.hotel.payment.enums.PaymentMethod;
import com.hotel.payment.enums.PaymentStatus;
import com.hotel.payment.notification_service.email_service.subjects.PaymentNotifier;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;

@Entity
@DiscriminatorValue("CARD")
public class CardPaymentModel extends Payment{
    @Transient
    private String cardNumber;

    public CardPaymentModel(String bookingID, String customerEmail, Double amount, String cardNumber){
        super(bookingID, customerEmail, amount, PaymentMethod.CARD);
        this.cardNumber = cardNumber;
    }

    public CardPaymentModel(){
        super("", "", 0.0, PaymentMethod.CARD);
    }


    @Override
    public void processPayment(){
        System.out.println("Payment processed via card" + bookingID + " "+ customerEmail); 
        this.status = PaymentStatus.DONE;
    }

    @Override
    public void refundPayment() {
        this.paymentMethod = PaymentMethod.CARD;
        System.out.println("Payment refunded for credit card payment");
        this.status = PaymentStatus.DONE;
    }


    @Override
    public String getBookingId() {
        return this.bookingID;
    }

    @Override
    public Double getAmount() {
        return this.amount;
    }

    @Override
    public String getcustomerEmail() {
        return this.customerEmail;
    }

    @Override
    public PaymentResponse notifyPayment(PaymentNotifier notifier){
        //Observer logic here
        notifier.notifyObservers(this, status.paymentstatusResponse()+" via "+paymentMethod.methodResponse());
        return new PaymentResponse(status.paymentstatusResponse()+" via "+paymentMethod.methodResponse(), status.paymentstatusResponse(), status.statusResult());
    }


    @Override
    public void setBookingId(String bookingID) {
        this.bookingID = bookingID;
    }


    @Override
    public void setAmount(Double amount) {
        this.amount = amount;
    }


    @Override
    public void setCustomerEmail(String email) {
        customerEmail = email;
    }

    @Override
    public void paymentMethodInfo() {
        System.out.println("Payment made via " + this.paymentMethod.name());
    }

    public void setCardNumber(String number){
        cardNumber = number;
    }

    @Override
    public PaymentResponse notifyRefund(PaymentNotifier notifier) {
        notifier.notifyObservers(this, status.refundStatusResponse()+" for "+PaymentMethod.CARD.methodResponse());
        return new PaymentResponse(status.refundStatusResponse()+" via "+PaymentMethod.CARD.methodResponse(), status.refundStatusResponse(),status.statusResult());
    }
}
