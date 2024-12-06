package com.hotel.payment.service;

import com.hotel.dto.request.PaymentRequest;
import com.hotel.dto.response.PaymentResponse;

public interface PaymentService {
    PaymentResponse processPayment(PaymentRequest request);
    PaymentResponse refundPayment(String id);
}
