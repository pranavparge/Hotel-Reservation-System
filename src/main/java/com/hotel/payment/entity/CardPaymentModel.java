package com.hotel.payment.entity;

import com.hotel.dto.response.PaymentResponse;
import com.hotel.enums.PaymentMethod;
import com.hotel.enums.PaymentStatus;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;

@Entity
@DiscriminatorValue("CARD")
public class CardPaymentModel extends Payment{
    /// The card number provided in the requests.
    @Transient
    private String cardNumber;
    /// The card name provided in the requests.
    @Transient
    private String cardName;
    /// The card cvv provided in the requests.
    @Transient
    private String cvv;
    /// The expiry of the card provided in the requests.
    @Transient
    private String expiry;

    public CardPaymentModel(String bookingID, String customerEmail, Double amount, String cardNumber){
        super(bookingID, customerEmail, amount, PaymentMethod.CARD);
        this.cardNumber = cardNumber;
    }

    public CardPaymentModel(){
        super("", "", 0.0, PaymentMethod.CARD);
    }


    /// Based on the details provided we can process the payments.
    /// We can utilise different packages to enable the card payments.
    @Override
    public void processPayment(){
        System.out.println("Payment processed via card" + bookingId + " "+ customerEmail);
        this.status = PaymentStatus.DONE;
    }

    /// Based on the [bookingId] provided we process the refund.
    /// In case of [PaymentMethod.CARD] payments based on the service-provider to deduct the charges incurred accoridingly and refund the appropriate refunds.
    @Override
    public void refundPayment() {
        this.paymentMethod = PaymentMethod.CARD;
        System.out.println("Payment refunded for credit card payment");
        this.status = PaymentStatus.DONE;
    }

    @Override
    public String getBookingId() {
        return this.bookingId;
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
        //IObserver logic here
        notifier.notifyObservers(this, status.paymentstatusResponse()+" via "+paymentMethod.methodResponse());
        return new PaymentResponse(status.paymentstatusResponse()+" via "+paymentMethod.methodResponse(), status.paymentstatusResponse(), status.statusResult());
    }

    @Override
    public void setBookingId(String bookingID) {
        this.bookingId = bookingID;
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
