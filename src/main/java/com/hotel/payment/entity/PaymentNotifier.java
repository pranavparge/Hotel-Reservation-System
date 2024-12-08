package com.hotel.payment.entity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

/// The notifier subject which implements the [ISubject]
/// All the necessary notification observers listen to this object.
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

    /// Notifies all the notification services which are [IObservers]
    /// Notifications made for the provided [Payment]
    /// [message] corresponds to the message that needs to be sent based on the scenario i.e Payment or Refund.
    @Override
    public void notifyObservers(Payment paymentModel, String message) {
       for (IObserver IObserver : IObservers) {
            IObserver.update(paymentModel, message);
       }
    }
    
}
