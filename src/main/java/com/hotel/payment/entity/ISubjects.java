package com.hotel.payment.entity;

/// This interface provides necessary methods to handle the observers specific to the subject.
/// Atttaching the different observers so that the changes in the payment can be notified according to the declared observers.
public interface ISubjects {
    /// Attaching the observers to this subject. 
    void attach(IObserver IObserver);
    /// Detaching the observers from this subject.
    void detach(IObserver IObserver);
    /// Method helps in notifying all the observers which are listening to the subjects implementing this interface.
    void notifyObservers(Payment paymentModel, String message);
}
