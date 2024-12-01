package com.hotel.payment.email_service.observers;

import com.hotel.payment.models.PaymentModel;

public interface Observer {
    void update(PaymentModel paymentModel);
}
