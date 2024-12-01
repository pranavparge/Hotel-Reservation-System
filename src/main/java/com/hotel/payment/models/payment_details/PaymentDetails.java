package com.hotel.payment.models.payment_details;

import com.hotel.payment.models.PaymentModel;

public interface PaymentDetails {
    public abstract void showPaymentDetails(); 
    public abstract PaymentModel createPaymentModel(double amount, String bookingID, String customerEmail);
}
