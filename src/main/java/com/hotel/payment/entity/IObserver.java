package com.hotel.payment.entity;

/// The iterface for notification observer
public interface IObserver {
    /// The method is reponsible to send notifications to the user based on the appropriate [IObserver]
    void update(Payment paymentModel, String message);
}
