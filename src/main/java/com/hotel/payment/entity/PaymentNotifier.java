package com.hotel.payment.entity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class PaymentNotifier implements ISubjects {
    private final List<IObserver> IObservers = new ArrayList<>();


    @Override
    public void attach(IObserver IObserver) {
        IObservers.add(IObserver);
    }

    @Override
    public void detach(IObserver IObserver) {
        IObservers.remove(IObserver);
    }

    @Override
    public void notifyObservers(Payment paymentModel, String message) {
       for (IObserver IObserver : IObservers) {
            IObserver.update(paymentModel, message);
       }
    }
    
}
