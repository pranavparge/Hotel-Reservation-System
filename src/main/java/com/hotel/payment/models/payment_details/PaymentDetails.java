package com.hotel.payment.models.payment_details;

import com.hotel.payment.models.Payment;

public interface PaymentDetails {
    public abstract void showPaymentDetails(); 
    public abstract Payment createPaymentModel(double amount, String bookingID, String customerEmail);
    public String validate();
}
