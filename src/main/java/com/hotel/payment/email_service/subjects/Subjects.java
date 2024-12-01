package com.hotel.payment.email_service.subjects;

import com.hotel.payment.email_service.observers.Observer;
import com.hotel.payment.models.PaymentModel;

public interface Subjects {
    void attach(Observer observer);
    void detach(Observer observer);
    void notifyObservers(PaymentModel paymentModel);
}
