package com.hotel.payment.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hotel.payment.dto.PaymentRequest;
import com.hotel.payment.dto.PaymentResponse;
import com.hotel.payment.service.PaymentService;

import com.hotel.util.Error;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {
    @Autowired
    private PaymentService paymentService;
    
    @PostMapping("/pay")
    public ResponseEntity<?> processPayment(@RequestBody Map<String,Object> data) {
        try {
            PaymentRequest request = new PaymentRequest(data);
            PaymentResponse response = paymentService.processPayment(request);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch(IllegalArgumentException exception){
            return new ResponseEntity<>(
                    new Error("Invalid payment request!", exception.getMessage(), HttpStatus.BAD_REQUEST.value()),
                    HttpStatus.BAD_REQUEST
            );
        } 
        catch (Exception e) {
            return new ResponseEntity<>(
                    new Error("Payment failure!", e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @PostMapping("/refund")
    public ResponseEntity<?> refundPayment(@RequestBody Map<String, Object> data) {
        try {
            String bookingid = data.get("id").toString();
            PaymentResponse response = paymentService.refundPayment(bookingid);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch(IllegalArgumentException exception){
            return new ResponseEntity<>(
                    new Error("Invalid payment refund request!", exception.getMessage(), HttpStatus.BAD_REQUEST.value()),
                    HttpStatus.BAD_REQUEST
            );
        } 
        catch (Exception e) {
            return new ResponseEntity<>(
                    new Error("Payment refund failure!", e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}
