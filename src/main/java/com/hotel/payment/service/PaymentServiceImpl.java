package com.hotel.payment.service;

import com.hotel.dto.request.PaymentRequest;
import com.hotel.dto.request.RefundPaymentRequest;
import com.hotel.dto.response.PaymentResponse;
import com.hotel.payment.factory.PaymentFactory;
import com.hotel.payment.models.PaymentModel;

public class PaymentServiceImpl implements PaymentService{

    @Override
    public PaymentResponse processPayment(PaymentRequest request) {
        try {
            final PaymentModel paymentModel = PaymentFactory.createPayment(request.getAmount(), request.bookingID(), request.getPaymentDetails());
            return paymentModel.processPayment();
        } catch (Exception e) {
            System.out.println("Failed to process the payment");
        }
        throw new UnsupportedOperationException("Unimplemented method 'processPayment'");
    }

    @Override
    public PaymentResponse refundPayment(RefundPaymentRequest request) {
        throw new UnsupportedOperationException("Unimplemented method 'refundPayment'");
    }
}
