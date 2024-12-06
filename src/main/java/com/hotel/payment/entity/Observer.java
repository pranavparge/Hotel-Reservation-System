package com.hotel.payment.entity;

public interface Observer {
    void update(Payment paymentModel, String message);
}
