package com.hotel.repository;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;

import com.hotel.payment.entity.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long>{
    @Query("select p from Payment p where p.bookingId = ?1")
    List<Payment> findPaymentByBookingId(String bookingId);
}
