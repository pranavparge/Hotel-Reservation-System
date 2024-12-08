package com.hotel.payment.entity;

import java.time.Instant;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import com.hotel.dto.response.PaymentResponse;
import com.hotel.enums.PaymentMethod;
import com.hotel.enums.PaymentStatus;

/// The class which holds all the necessary attributes to process the payment
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long paymentId;

    /// Timestamp of the payment/refund request
    @CreationTimestamp
    private Instant timeStamp;

    /// The appropriate payment method corresponsing to [PaymentMethod] provided according to the requests. 
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    public PaymentMethod paymentMethod;

    /// To maintain the status of transactions corresponsing to the [PaymentStatus].
    @Enumerated(EnumType.STRING)
    @Transient
    public PaymentStatus status;

    /// The booking ID of the customer
    public String bookingId;

    /// Total cost for the booking
    public double amount;

    /// The customer email, to send notification of the payments/refunds.
    @Transient
    public String customerEmail;

    public Payment(String bookingID, String customerEmail, Double amount, PaymentMethod paymentMethod) {
        this.amount = amount;
        this.bookingId = bookingID;
        this.customerEmail = customerEmail;
        this.paymentMethod = paymentMethod;
    }

    /// To process the payment according the [PaymentMethod] mentioned in the requests.
    public abstract void processPayment();
    /// To process the refunds according the [PaymentMethod] mentioned in the requests
    public abstract void refundPayment();
    /// fetch [bookingId]
    public abstract String getBookingId();
    /// fetch [amount]
    public abstract Double getAmount();
    /// fetch [customerEmail]
    public abstract String getcustomerEmail();
    /// Update [bookingId], used for testing purpose
    /// In general scenarios we are restricted to update the [bookingId]. 
    public abstract void setBookingId(String bookingID);
    /// Update [amount], used for testing purposes
    /// In general scenarios we are restricted to update the [amount].
    public abstract void setAmount(Double amount);
    /// Update [customerEmail]
    /// In general scenarios we are restricted to update the [customerEmail]. 
    public abstract void setCustomerEmail(String email);
    /// To fetch the payment info of the extending objects
    public abstract void paymentMethodInfo();
    /// Triggers the notification of the payments made to the customer via [IObserver]
    /// Provides a [PaymentResponse] of the result of the payment which is sent as the HTTP response for notifying the user regarding the success of the payment.
    public abstract PaymentResponse notifyPayment(PaymentNotifier notifier);
    /// Triggers the notification of the refunds made to the customer via [IObserver]
    /// Provides a [PaymentResponse] of the result of the refund which is sent as the HTTP response for notifying the user regarding the success of the refund request.
    public abstract PaymentResponse notifyRefund(PaymentNotifier notifier);
}
