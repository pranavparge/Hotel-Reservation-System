package com.hotel.payment.email_service.subjects;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.hotel.payment.email_service.observers.Observer;
import com.hotel.payment.models.PaymentModel;

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
    public void notifyObservers(PaymentModel paymentModel) {
       for (Observer observer : observers) {
            observer.update(paymentModel);;
       }
    }
    
}
