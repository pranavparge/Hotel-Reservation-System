package com.hotel.payment.service;

import com.hotel.dto.request.PaymentRequest;
import com.hotel.dto.request.RefundPaymentRequest;
import com.hotel.dto.response.PaymentResponse;

public interface PaymentService {
    void processPayment(PaymentRequest request);
    void refundPayment(RefundPaymentRequest request);
}
