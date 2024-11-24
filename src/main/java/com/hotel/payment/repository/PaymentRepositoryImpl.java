package com.hotel.payment.repository;

import java.util.concurrent.ExecutionException;

import com.hotel.dto.response.PaymentResponse;
import com.hotel.payment.models.CardPaymentModel;
import com.hotel.payment.models.PaymentModel;

public class PaymentRepositoryImpl implements PaymentRepository {

    @Override
    public boolean processCardPayment(double amount, String cardNumber) {
        try {
            System.out.println("Process card payment here");
            return true;
        } catch (Exception e) {
           throw new org.hibernate.sql.exec.ExecutionException(e.toString());
        }
       
    }

    @Override
    public boolean processPaypalPayment(double amount, String paypalid) {
        try {
            System.out.println("Process paypal payment here");
            return true;
        } catch (Exception e) {
           throw new org.hibernate.sql.exec.ExecutionException(e.toString());
        }
    }

    @Override
    public void processRefund() {
        System.out.println("Process refunds here");
        throw new UnsupportedOperationException("Unimplemented method 'processRefund'");
    }
    
}
