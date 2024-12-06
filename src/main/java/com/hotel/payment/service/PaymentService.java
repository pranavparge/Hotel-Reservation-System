package com.hotel.payment.service;

import com.hotel.payment.dto.PaymentRequest;
import com.hotel.payment.dto.PaymentResponse;

public interface PaymentService {
    PaymentResponse processPayment(PaymentRequest request);
    PaymentResponse refundPayment(String id);
}
