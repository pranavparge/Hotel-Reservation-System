package com.hotel.payment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hotel.payment.models.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long>{
    @Query("select p from Payment p where p.bookingID = ?1")
    List<Payment> findPaymentByBookingID(String bookingID);
}
