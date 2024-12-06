package com.hotel.payment.entity;

public interface ISubjects {
    void attach(IObserver IObserver);
    void detach(IObserver IObserver);
    void notifyObservers(Payment paymentModel, String message);
}
