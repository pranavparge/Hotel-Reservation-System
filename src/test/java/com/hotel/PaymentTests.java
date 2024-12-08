package com.hotel;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotel.dto.request.PaymentRequest;
import com.hotel.dto.response.PaymentResponse;
import com.hotel.payment.service.PaymentService;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
/// Test class for Payment  Service
public class PaymentTests {
    /// Inistialisation of payment service
    @MockBean
    private PaymentService paymentService;


    @Autowired
    private MockMvc mockMvc;

    /// Method to test the card payment requests.
    @Test
    void testCardPaymentEndpoint() throws Exception {

        PaymentResponse response = new PaymentResponse("Payment successfull via Card", "Payment successfull", true);

        Mockito.when(paymentService.processPayment(any(PaymentRequest.class))).thenReturn(response);

        String cardPaymentRequest = "{\"bookingId\":\"2123\",\"amount\":120.0,\"customerEmail\":\"customer@gmail.com\",\"paymentMethod\":\"Card\",\"details\":{\"cardNumber\":\"1234567812345678\",\"cardName\":\"abc\",\"expiry\":\"12/25\",\"cvv\":\"123\"}}"
;

        mockMvc.perform(post("/customer/pay")
                .header("Authorization", "Bearer "+jwtToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(cardPaymentRequest))
                .andExpect(status().isOk()) // Expect 200 OK
                .andExpect(jsonPath("$.result").value(true)) 
                .andExpect(jsonPath("$.status").value("Payment successfull")) 
                .andExpect(jsonPath("$.message").value("Payment successfull via Card")) 
                .andExpect(jsonPath("$.timeStamp").exists()); 
    }

    /// Method to test the wallet (Paypal) payment requests.
    @Test
    void testWalletPaymentEndpoint() throws Exception {

        PaymentResponse response = new PaymentResponse("Payment successfull via Paypal", "Payment successfull", true);

        Mockito.when(paymentService.processPayment(any(PaymentRequest.class))).thenReturn(response);

        String cardPaymentRequest = "{\"bookingId\":\"2124\",\"amount\":120.0,\"customerEmail\":\"customer@gmail.com\",\"paymentMethod\":\"Paypal\",\"details\":{\"paypalID\":\"1234567812345678\"}}"
;

        mockMvc.perform(post("/customer/pay")
                .header("Authorization", "Bearer "+jwtToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(cardPaymentRequest))
                .andExpect(status().isOk()) // Expect 200 OK
                .andExpect(jsonPath("$.result").value(true)) 
                .andExpect(jsonPath("$.status").value("Payment successfull")) 
                .andExpect(jsonPath("$.message").value("Payment successfull via Paypal")) 
                .andExpect(jsonPath("$.timeStamp").exists()); 
    }

    /// Method to test the card refund payment requests.
    @Test
    void testCardRefundEndpoint() throws Exception {

        PaymentResponse response = new PaymentResponse("Refund processed successfully via Card", "Refund processed successfully", true);

        Mockito.when(paymentService.refundPayment(any(String.class))).thenReturn(response);

        String cardRefundRequest = "{\"id\":\"2123\"}"
;

        mockMvc.perform(post("/customer/refund")
                .header("Authorization", "Bearer "+jwtToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(cardRefundRequest))
                .andExpect(status().isOk()) // Expect 200 OK
                .andExpect(jsonPath("$.result").value(true)) 
                .andExpect(jsonPath("$.status").value("Refund processed successfully")) 
                .andExpect(jsonPath("$.message").value("Refund processed successfully via Card")) 
                .andExpect(jsonPath("$.timeStamp").exists()); 
    }

    /// Method to test the wallet (Paypal) refund requests.
    @Test
    void testWalletRefundEndpoint() throws Exception {

        PaymentResponse response = new PaymentResponse("Refund processed successfully via Paypal", "Refund processed successfully", true);

        Mockito.when(paymentService.refundPayment(any(String.class))).thenReturn(response);

        String paypalRefundRequest = "{\"id\":\"2124\"}"
;

        mockMvc.perform(post("/customer/refund")
                .header("Authorization", "Bearer "+jwtToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(paypalRefundRequest))
                .andExpect(status().isOk()) // Expect 200 OK
                .andExpect(jsonPath("$.result").value(true)) 
                .andExpect(jsonPath("$.status").value("Refund processed successfully")) 
                .andExpect(jsonPath("$.message").value("Refund processed successfully via Paypal")) 
                .andExpect(jsonPath("$.timeStamp").exists()); 
    }

    /// Method to generate the JWT token required for authentication
    public AtomicReference<String> jwtToken() throws Exception {
       AtomicReference<String> jwtToken = new AtomicReference<>("");

       String jsonPayload = "{ \"email\": \"customer@gmail.com\", \"password\": \"customer@123\" }";

       mockMvc.perform(post("/auth/customer/sign-in")
                       .contentType("application/json")
                       .content(jsonPayload))
               .andExpect(status().isOk()) 
               .andExpect(jsonPath("$.jwt").exists())
               .andDo(result -> {
                   String responseBody = result.getResponse().getContentAsString();
                   ObjectMapper objectMapper = new ObjectMapper();
                   JsonNode responseJson = objectMapper.readTree(responseBody);

                   jwtToken.set(responseJson.get("jwt").asText());

                   System.out.println("Extracted JWT Token: " + jwtToken);
               });

       return jwtToken;

    }



}


