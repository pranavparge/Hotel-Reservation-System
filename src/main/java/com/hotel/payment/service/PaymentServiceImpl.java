package com.hotel.payment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hotel.dto.request.PaymentRequest;
import com.hotel.dto.request.RefundPaymentRequest;
import com.hotel.dto.response.PaymentResponse;
import com.hotel.enums.PaymentStatus;
import com.hotel.payment.email_service.subjects.PaymentNotifier;
import com.hotel.payment.factory.PaymentFactory;
import com.hotel.payment.models.PaymentModel;
import com.hotel.payment.repository.PaymentRepositoryImpl;

@Service
public class PaymentServiceImpl implements PaymentService{
    final PaymentRepositoryImpl paymentRepository = new PaymentRepositoryImpl();

    @Autowired
    private PaymentNotifier paymentNotifier;

    @Autowired
    private PaymentFactory paymentFactory;

    @Override
    public void processPayment(PaymentRequest request) {
        try {
            final PaymentModel paymentModel = paymentFactory.createPayment(request.getAmount(), request.bookingID(), request.getEmail(), request.getPaymentDetails());
            PaymentResponse response = paymentRepository.savePayment(paymentModel);
            if(response.getPaymentStatus() == PaymentStatus.DONE){
                paymentNotifier.notifyObservers(paymentModel);
            }
        } catch (Exception e) {
            System.out.println("Failed to process the payment");
        }
    }

    @Override
    public void refundPayment(RefundPaymentRequest request) {
        throw new UnsupportedOperationException("Unimplemented method 'refundPayment'");
    }
}
