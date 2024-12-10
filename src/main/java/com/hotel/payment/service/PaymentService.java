package com.hotel.payment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hotel.dto.request.PaymentRequest;
import com.hotel.dto.response.PaymentResponse;
import com.hotel.payment.entity.PaymentStrategy;
import com.hotel.payment.entity.Payment;
import com.hotel.payment.entity.PaymentNotifier;
import com.hotel.repository.PaymentRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class PaymentService implements IPaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentNotifier paymentNotifier;

    @Autowired
    private PaymentStrategy paymentStrategy;

    /// Making them thread safe to avoid any unecessary complications while making the payments.
    /// Avoids making the dubious payments thereby following ACID compliant.
    @Override
    synchronized public PaymentResponse processPayment(PaymentRequest request) {
        
        String validString = request.validate();
        if(!Objects.equals(validString, "")){
            throw new IllegalArgumentException(validString);
        }
        String validDetails = request.getPaymentDetails().validate();
        if(!Objects.equals(validDetails, "")){
            throw new IllegalArgumentException(validDetails);
        }
        final Payment paymentModel = paymentStrategy.createPayment(request.getAmount(), request.bookingId(), request.getEmail(), request.getPaymentDetails());
        paymentModel.processPayment();
        paymentRepository.save(paymentModel);
        return paymentModel.notifyPayment(paymentNotifier);
    }

     /// Making them thread safe to avoid any unecessary complications while making the payments.
    /// Avoids making the dubious refunds thereby following ACID compliant.
    @Override
    synchronized public PaymentResponse refundPayment(String id) {
    
        if(id == ""){
            throw new IllegalArgumentException("Booking id not provided");
        }
        List<Payment> modelList = paymentRepository.findPaymentByBookingId(id);
        if(modelList.isEmpty()){
            throw new IllegalStateException("No payment record found to process the refund");
        }
        Payment model = modelList.get(0);
        model.refundPayment();
        
        paymentRepository.delete(model);
        
        return model.notifyRefund(paymentNotifier);
       
    }
}
