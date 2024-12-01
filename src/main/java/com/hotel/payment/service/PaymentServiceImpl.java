package com.hotel.payment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import com.hotel.dto.request.PaymentRequest;
import com.hotel.dto.request.RefundPaymentRequest;
import com.hotel.dto.response.PaymentResponse;
import com.hotel.enums.PaymentStatus;
import com.hotel.payment.email_service.subjects.PaymentNotifier;
import com.hotel.payment.factory.PaymentFactory;
import com.hotel.payment.models.PaymentModel;
import com.hotel.payment.repository.PaymentRepository;
import com.hotel.payment.repository.PaymentRepositoryImpl;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService{
    final PaymentRepository paymentRepository = new PaymentRepository();

    @Autowired
    private PaymentNotifier paymentNotifier;

    @Autowired
    private PaymentFactory paymentFactory;

    @Override
    public PaymentResponse processPayment(PaymentRequest request) {
        try {
            final PaymentModel paymentModel = paymentFactory.createPayment(request.getAmount(), request.bookingID(), request.getEmail(), request.getPaymentDetails());
            model.processPayment();
            PaymentModel model = paymentRepository.save(paymentModel);
            model.notifyCustomer("Payment done successfully")
            return model.paymentResponse();
        } catch (Exception e) {
            System.out.println("Failed to process the payment");
        }
    }

    @Override
    public PaymentResponse refundPayment(String id) {
        try {
            final PaymentModel model = paymentRepository.findPaymentByBookingID(id);
            boolean result = model.refundPayment();
            if(result){
                paymentRepository.delete(model);
            }
            model.notifyCustomer("Payment refund done successfully");
        } catch (Exception e) {
            System.out.println("Failed to refund the payment");
        }
    }
}
