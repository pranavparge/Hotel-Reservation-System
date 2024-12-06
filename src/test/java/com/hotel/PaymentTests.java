package com.hotel;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.hotel.payment.controller.PaymentController;
import com.hotel.dto.request.PaymentRequest;
import com.hotel.dto.response.PaymentResponse;
import com.hotel.enums.PaymentMethod;
import com.hotel.repository.PaymentRepository;
import com.hotel.payment.service.PaymentService;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PaymentTests {
    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentController paymentController;

    @Autowired
    private MockMvc mockMvc;

    private Map<String, Object> createPaymentDetailsObjectCard(String cardNumber, String cardName, String expiry, String cvv){
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("cardNumber", cardNumber);
        result.put("cardName", cardName);
        result.put("expiry", expiry);
        result.put("cvv", cvv);
        return result;
    }

    private Map<String, Object> createPaymentDetailsObjectPaypal(String paypalID){
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("id", paypalID);
        return result;
    }

    private Map<String, Object> createPaymentRequest(PaymentMethod method){
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("bookingID", "2112");
        result.put("amount", 120.0);
        result.put("customerEmail", "customer@gmail.com");
        result.put("details", (method == PaymentMethod.CARD)?createPaymentDetailsObjectCard("123-456-789", "Customer", "12/25", "123"):createPaymentDetailsObjectPaypal("123#245"));
        result.put("paymentMethod", (method == PaymentMethod.CARD)?"Card":"Paypal");
        return result;
    }

   @Test
   void testProcessPayment() {
       // {
       //     "bookingID": "2123",
       //     "amount": 120.0,
       //     "customerEmail": "customer@gmail.com",
       //     "paymentMethod": "Paypal",
       //     "details": {
       //       "paypalID": "1234567812345678"
       //       }
       //   }
       PaymentRequest paymentRequest = new PaymentRequest(createPaymentRequest(PaymentMethod.CARD));
       PaymentResponse response = paymentService.processPayment(paymentRequest);
       System.out.println("response "+ response.toString());
       assertNotNull(response);
//        assertTrue(response.getResult());
//        assertEquals("Payment Successfull", response.getStatus());
   }

    @Test
    void testPaymentEndpoint(){
        String requestBody = "{ \"bookingID\": \"B123\", \"amount\": 100.0, \"paymentMethod\": \"Paypal\", \"details\": {\"paypalID\": \"1234-5678-9876\"}}";

        try {
            mockMvc.perform(post("/payment/pay")
                    // .accept(MediaType.APPLICATION_JSON).headers(HttpHeaders.AUTHORIZATION=)
                    .content(requestBody))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.result").value(true));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}


