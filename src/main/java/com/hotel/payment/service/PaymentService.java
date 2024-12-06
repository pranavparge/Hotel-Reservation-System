package com.hotel.payment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hotel.dto.request.PaymentRequest;
import com.hotel.dto.response.PaymentResponse;
import com.hotel.payment.entity.PaymentFactory;
import com.hotel.payment.entity.Payment;
import com.hotel.payment.entity.PaymentNotifier;
import com.hotel.repository.PaymentRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PaymentService implements IPaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentNotifier paymentNotifier;

    @Autowired
    private PaymentFactory paymentFactory;

    @Override
    public PaymentResponse processPayment(PaymentRequest request) {
        try {
            String validString = request.validate();
            if(validString != ""){
                throw new IllegalArgumentException(validString);
            }
            String validDetails = request.getPaymentDetails().validate();
            if(validDetails != ""){
                throw new IllegalArgumentException(validDetails);
            }
            if(!paymentRepository.findPaymentByBookingID(request.getBookingId()).isEmpty()){
                throw new IllegalStateException("Payment has been processed");
            }
            final Payment paymentModel = paymentFactory.createPayment(request.getAmount(), request.bookingID(), request.getEmail(), request.getPaymentDetails());
            paymentModel.processPayment();
            paymentRepository.save(paymentModel);
            return paymentModel.notifyPayment(paymentNotifier);
        } catch (Exception e) {
            return new PaymentResponse(e.getMessage(), "Payment failure",false);
        }


    }

    @Override
    public PaymentResponse refundPayment(String id) {
        try{
            if(id == ""){
                throw new IllegalArgumentException("Booking id not provided");
            }
            List<Payment> modelList = paymentRepository.findPaymentByBookingID(id);
            if(modelList.isEmpty()){
                throw new IllegalStateException("No payment record found to process the refund");
                
            }
            Payment model = modelList.get(0);
            model.refundPayment();
            
            paymentRepository.delete(model);
           
            return model.notifyRefund(paymentNotifier);
        } catch (Exception e) {
            return new PaymentResponse(e.getMessage(), "Refund failure",false);
        }
    }
}
