package com.hotel.payment.entity;

import java.time.Instant;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import com.hotel.dto.response.PaymentResponse;
import com.hotel.enums.PaymentMethod;
import com.hotel.enums.PaymentStatus;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long paymentId;

    @CreationTimestamp
    private Instant timeStamp;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    public PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Transient
    public PaymentStatus status;

    public String bookingId;

    public double amount;

    @Transient
    public String customerEmail;

    public Payment(String bookingID, String customerEmail, Double amount, PaymentMethod paymentMethod) {
        this.amount = amount;
        this.bookingId = bookingID;
        this.customerEmail = customerEmail;
        this.paymentMethod = paymentMethod;
    }

    public abstract void processPayment();
    public abstract void refundPayment();
    public abstract String getBookingId();
    public abstract Double getAmount();
    public abstract String getcustomerEmail();
    public abstract void setBookingId(String bookingID);
    public abstract void setAmount(Double amount);
    public abstract void setCustomerEmail(String email);
    public abstract void paymentMethodInfo();
    public abstract PaymentResponse notifyPayment(PaymentNotifier notifier);
    public abstract PaymentResponse notifyRefund(PaymentNotifier notifier);
}
