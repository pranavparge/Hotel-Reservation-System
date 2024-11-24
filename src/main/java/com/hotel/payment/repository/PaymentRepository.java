package com.hotel.payment.repository;

import org.springframework.stereotype.Repository;

import com.hotel.dto.response.PaymentResponse;
import com.hotel.payment.models.CardPaymentModel;
import com.hotel.payment.models.PaymentModel;
import com.hotel.payment.models.PaypalPaymentModel;

@Repository
public interface PaymentRepository {
    public PaymentResponse savePayment(PaymentModel paymentModel);
    public void processRefund();
}
