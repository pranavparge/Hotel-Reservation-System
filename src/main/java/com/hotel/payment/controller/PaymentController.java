package com.hotel.payment.controller;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hotel.dto.request.PaymentRequest;
import com.hotel.dto.response.PaymentResponse;
import com.hotel.payment.service.IPaymentService;

import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
public class PaymentController {
    @Autowired
    private IPaymentService IPaymentService;
    /// Helps apply application level locking to prevent duplicate requests happening
    /// Check if the booking is already under process and throws an error if so
    private final ConcurrentHashMap<String, Boolean> processingBookings = new ConcurrentHashMap<>();
    
    @PostMapping("/customer/pay")
    public ResponseEntity<?> processPayment(@RequestBody Map<String,Object> data) {
        PaymentRequest request = new PaymentRequest(data);
        try {
            if (processingBookings.putIfAbsent(request.bookingId(), true) != null) {
                throw new IllegalStateException("Payment is already under process");
            }
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
        catch(IllegalStateException exception){
            PaymentResponse response = new PaymentResponse("Payment is already processing!", "Payment failure!", false);
            return new ResponseEntity<>(
                    response,
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
        catch (Exception e) {
            PaymentResponse response = new PaymentResponse(e.getMessage(), "Payment failure!", false);
            return new ResponseEntity<>(
                    response,
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
        finally{
            // remove the bookingID
            processingBookings.remove(request.bookingId());
        }
    }

    @PostMapping("/customer/refund")
    public ResponseEntity<?> refundPayment(@RequestBody Map<String, Object> data) {
        String bookingid = data.get("id").toString();
        try {
           
            if (processingBookings.putIfAbsent(bookingid, true) != null) {
                throw new IllegalStateException("Payment is already under process");
            }
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
        catch(IllegalStateException exception){
            PaymentResponse response = new PaymentResponse("Refund is already processing!", "Payment failure!", false);
            return new ResponseEntity<>(
                    response,
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
        catch (Exception e) {
            PaymentResponse response = new PaymentResponse(e.getMessage(), "Payment refund failure!", false);
            return new ResponseEntity<>(
                    response,
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
        finally{
            // remove the bookingID
            processingBookings.remove(bookingid);
        }
    }
}
