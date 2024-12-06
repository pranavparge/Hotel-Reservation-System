package com.hotel.payment.entity;

public interface IPaymentDetails {
    public abstract void showPaymentDetails(); 
    public abstract Payment createPaymentModel(double amount, String bookingID, String customerEmail);
    public String validate();
}
