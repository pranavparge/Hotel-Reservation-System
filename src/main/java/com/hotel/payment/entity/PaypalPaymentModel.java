package com.hotel.payment.entity;

import com.hotel.dto.response.PaymentResponse;
import com.hotel.enums.PaymentMethod;
import com.hotel.enums.PaymentStatus;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("PAYPAL")
public class PaypalPaymentModel extends Payment{
    /// The paypal id via which the paypal wallet payments/refunds would be made.
    @Transient
    private String paypalID;

    public PaypalPaymentModel(String bookingID, String customerEmail, Double amount, String paypalID){
        super(bookingID, customerEmail, amount, PaymentMethod.PAYPAL);
        this.paypalID = paypalID;
    }

    public PaypalPaymentModel(){
        super("", "", 0.0, PaymentMethod.PAYPAL);
    }

    /// Process the payments via Paypal
    /// We can utilise any Paypal package for Java spring to successfully complete the payment end-to-end.
    @Override
    public void processPayment(){
        System.out.println("Payment processed via paypal");
        this.status = PaymentStatus.DONE;
    }

    /// Process the refunds for the paypal payments.
    /// Make the appropriate refunds based on the business conditionsa and requirements.
    @Override
    public void refundPayment() {
        this.paymentMethod = PaymentMethod.PAYPAL;
        System.out.println("Payment refunded for paypal payment");
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
        return new PaymentResponse(status.paymentstatusResponse()+" via "+paymentMethod.methodResponse(), status.paymentstatusResponse(),status.statusResult());
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
        System.out.println("Payment made via "+this.paymentMethod.name());
    }

    public void setPaypalID(String number){
        paypalID = number;
    }

    @Override
    public PaymentResponse notifyRefund(PaymentNotifier notifier) {
        notifier.notifyObservers(this, status.refundStatusResponse()+" for "+PaymentMethod.PAYPAL.methodResponse());
        return new PaymentResponse(status.refundStatusResponse()+" via "+PaymentMethod.PAYPAL.methodResponse(), status.refundStatusResponse(),status.statusResult());
    }
}
