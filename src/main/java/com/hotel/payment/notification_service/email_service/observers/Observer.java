package com.hotel.payment.notification_service.email_service.observers;

import com.hotel.payment.models.Payment;

public interface Observer {
    void update(Payment paymentModel, String message);
}
