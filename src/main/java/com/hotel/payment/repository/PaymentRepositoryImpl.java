package com.hotel.payment.repository;

import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Repository;

import com.hotel.dto.response.PaymentResponse;
import com.hotel.payment.models.CardPaymentModel;
import com.hotel.payment.models.PaymentModel;
import com.hotel.payment.models.PaypalPaymentModel;

@Repository
public class PaymentRepositoryImpl implements PaymentRepository {

    @Override
    public PaymentResponse savePayment(PaymentModel paymentModel) {
        try {
            System.out.println("Process card payment here");
            final boolean result = paymentModel.processPayment();
            PaymentResponse response = new PaymentResponse("1", paymentModel.getBookingID(), paymentModel.getAmount(), paymentModel.getcustomerEmail(), result);
            return response;
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
