package com.hotel.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hotel.dto.response.PaymentResponse;
import com.hotel.payment.models.CardPaymentModel;
import com.hotel.payment.models.PaymentModel;
import com.hotel.payment.models.PaypalPaymentModel;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentModel, Long>{
    @Query("SELECT p FROM PaymentModel p WHERE p.bookingID = :bookingID")
    PaymentModel findPaymentByBookingID(@Param("bookingID") String bookingID);
}
