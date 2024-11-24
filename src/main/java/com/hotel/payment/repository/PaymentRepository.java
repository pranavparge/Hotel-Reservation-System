package com.hotel.payment.repository;

import org.springframework.stereotype.Repository;

import com.hotel.dto.response.PaymentResponse;
import com.hotel.payment.models.PaymentModel;

@Repository
public interface PaymentRepository {
    public boolean processCardPayment(double amount, String cardNumber);
    public boolean processPaypalPayment(double amount, String paypalId);
    public void processRefund();
}
