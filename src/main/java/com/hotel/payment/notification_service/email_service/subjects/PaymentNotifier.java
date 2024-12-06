package com.hotel.payment.notification_service.email_service.subjects;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.hotel.payment.models.Payment;
import com.hotel.payment.notification_service.email_service.observers.Observer;

@Component
public class PaymentNotifier implements Subjects {
    private final List<Observer> observers = new ArrayList<>();


    @Override
    public void attach(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void detach(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(Payment paymentModel, String message) {
       for (Observer observer : observers) {
            observer.update(paymentModel, message);
       }
    }
    
}
