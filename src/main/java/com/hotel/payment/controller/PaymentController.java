package com.hotel.payment.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hotel.dto.request.CustomerSignUpRequest;
import com.hotel.dto.request.PaymentRequest;
import com.hotel.dto.request.RefundPaymentRequest;
import com.hotel.dto.request.StaffSignUpRequest;
import com.hotel.dto.response.CustomerSignUpResponse;
import com.hotel.dto.response.PaymentResponse;
import com.hotel.dto.response.StaffSignUpResponse;
import com.hotel.payment.service.PaymentService;

import jakarta.persistence.EntityExistsException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {
   @Autowired
    private PaymentService paymentService;
    
    @PostMapping("/pay")
    public ResponseEntity<?> processPayment(Map<String, Object> data) {
        try {
            PaymentRequest request = new PaymentRequest(data);
            PaymentResponse paymentResponse = paymentService.processPayment(request);
            return new ResponseEntity<>(paymentResponse, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Customer not created!", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/refund")
    public ResponseEntity<?> refundPayment(@Valid @RequestBody RefundPaymentRequest request) {
        try {
            PaymentResponse paymentResponse = paymentService.refundPayment(request);
            return new ResponseEntity<>(paymentResponse, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Customer not created!", HttpStatus.BAD_REQUEST);
        }
    }
}
