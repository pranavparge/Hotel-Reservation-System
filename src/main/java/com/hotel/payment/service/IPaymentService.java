package com.hotel.payment.service;

import com.hotel.dto.request.PaymentRequest;
import com.hotel.dto.response.PaymentResponse;

/// The payment service which handles all the business logic for our usecases.
public interface IPaymentService {
    /// Enables processing the payment based on the requests incoming.
    /// We make the check for all necessary validations for going forward with the transaction.
    PaymentResponse processPayment(PaymentRequest request);
    /// Enables the processing of refunds based on the requests incoming.
    PaymentResponse refundPayment(String id);
}
