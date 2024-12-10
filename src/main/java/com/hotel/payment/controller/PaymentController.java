package com.hotel.payment.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hotel.dto.request.PaymentRequest;
import com.hotel.dto.response.PaymentResponse;
import com.hotel.payment.service.IPaymentService;

import com.hotel.util.Error;

import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
public class PaymentController {
    @Autowired
    private IPaymentService IPaymentService;
    
    @PostMapping("/customer/pay")
    public ResponseEntity<?> processPayment(@RequestBody Map<String,Object> data) {
        try {
            PaymentRequest request = new PaymentRequest(data);
            PaymentResponse response = IPaymentService.processPayment(request);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch(IllegalArgumentException exception){
            PaymentResponse response = new PaymentResponse("Invalid payment request!", "Payment failure!", false);
            return new ResponseEntity<>(
                    response,
                    HttpStatus.BAD_REQUEST
            );
        } 
        catch (Exception e) {
            PaymentResponse response = new PaymentResponse(e.getMessage(), "Payment failure!", false);
            return new ResponseEntity<>(
                    response,
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @PostMapping("/customer/refund")
    public ResponseEntity<?> refundPayment(@RequestBody Map<String, Object> data) {
        try {
            String bookingid = data.get("id").toString();
            PaymentResponse response = IPaymentService.refundPayment(bookingid);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch(IllegalArgumentException exception){
            PaymentResponse response = new PaymentResponse("Invalid payment refund request!", "Payment refund failure!", false);
            return new ResponseEntity<>(
                    response,
                    HttpStatus.BAD_REQUEST
            );
        } 
        catch (Exception e) {
            PaymentResponse response = new PaymentResponse(e.getMessage(), "Payment refund failure!", false);
            return new ResponseEntity<>(
                    response,
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}
