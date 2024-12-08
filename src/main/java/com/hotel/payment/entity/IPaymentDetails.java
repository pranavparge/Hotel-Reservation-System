package com.hotel.payment.entity;

/// Interface of Payment Details 
public interface IPaymentDetails {
    /// To display the payment details
    public abstract void showPaymentDetails(); 
    /// Generate the appropriate [Payment] based on the payment request object provided.
    public abstract Payment createPaymentModel(double amount, String bookingID, String customerEmail);
    /// Validating the payment details request
    public String validate();
}
