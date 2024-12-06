package com.hotel.payment.entity;

public interface IObserver {
    void update(Payment paymentModel, String message);
}
