package com.hotel.payment.notification_service.email_service.subjects;

import com.hotel.payment.models.Payment;
import com.hotel.payment.notification_service.email_service.observers.Observer;

public interface Subjects {
    void attach(Observer observer);
    void detach(Observer observer);
    void notifyObservers(Payment paymentModel, String message);
}
