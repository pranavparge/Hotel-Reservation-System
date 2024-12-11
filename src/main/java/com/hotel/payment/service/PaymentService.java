package com.hotel.payment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hotel.dto.request.PaymentRequest;
import com.hotel.dto.response.PaymentResponse;
import com.hotel.payment.entity.PaymentStrategy;
import com.hotel.payment.entity.Payment;
import com.hotel.payment.entity.PaymentNotifier;
import com.hotel.repository.CustomerRepository;
import com.hotel.repository.PaymentRepository;
import com.hotel.user.entity.Customer;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
public class PaymentService implements IPaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private CustomerRepository customerRepository;

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
        List<Payment> payment = paymentRepository.findPaymentByBookingId(request.getbookingId());
        if(!payment.isEmpty()){
            throw new IllegalStateException("Payment already processed");
        }
        final Payment paymentModel = paymentStrategy.createPayment(request.getAmount(), request.getbookingId(), request.getEmail(), request.getPaymentDetails());
        paymentModel.processPayment();
        paymentRepository.save(paymentModel);
        return paymentModel.notifyPayment(paymentNotifier);
    }

     /// Making them thread safe to avoid any unecessary complications while making the payments.
    /// Avoids making the dubious refunds thereby following ACID compliant.
    @Override
    synchronized public PaymentResponse refundPayment(String bookingId, String customerId) {
    
        if(bookingId == ""){
            throw new IllegalArgumentException("Booking id not provided");
        }
        if(customerId == ""){
            throw new IllegalArgumentException("Customer id not provided");
        }
        List<Payment> modelList = paymentRepository.findPaymentByBookingId(bookingId);
        Optional<Customer> customer = customerRepository.findById(Long.parseLong(customerId));

        if(modelList.isEmpty()){
            throw new IllegalStateException("No payment record found to process the refund");
        }
        if(customer.isEmpty()){
            throw new IllegalStateException("No valid customer found!");
        }
        Payment model = modelList.get(0);
        model.setCustomerEmail(customer.get().getEmail());
        model.refundPayment();
        
        paymentRepository.delete(model);
        
        return model.notifyRefund(paymentNotifier);
       
    }
}
