package com.hotel.payment.entity;

public interface Subjects {
    void attach(Observer observer);
    void detach(Observer observer);
    void notifyObservers(Payment paymentModel, String message);
}
