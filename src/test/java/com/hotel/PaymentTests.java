package com.hotel;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import com.hotel.util.JwtUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotel.dto.request.PaymentRequest;
import com.hotel.dto.response.PaymentResponse;
import com.hotel.enums.ProgramType;
import com.hotel.payment.service.PaymentService;
import com.hotel.repository.CustomerRepository;
import com.hotel.repository.StaffRepository;
import com.hotel.user.entity.Customer;
import com.hotel.user.entity.Staff;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
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

    @MockBean
    private CustomerRepository customerRepository;

    @MockBean
    private StaffRepository staffRepository;

    @MockBean
    private JwtUtility jwtUtility;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setupMocks() {
        when(customerRepository.findFirstByEmail("customer@gmail.com")).thenReturn(Optional.of(
                new Customer(ProgramType.MEMBER, "CustomerName", "customer@gmail.com", "{noop}password")
        ));
        when(staffRepository.findFirstByEmail("staff@gmail.com")).thenReturn(Optional.of(
                new Staff("StaffName", "staff@gmail.com", "{noop}password")
        ));
    }

    /// Method to test the card payment requests.
    @Test
    void testCardPaymentEndpoint() throws Exception {
        when(jwtUtility.isTokenValid(anyString(), any(UserDetails.class))).thenReturn(true);
        when(jwtUtility.extractUsername(anyString())).thenReturn("customer@gmail.com");
        when(jwtUtility.extractRole(anyString())).thenReturn("CUSTOMER");

        PaymentResponse response = new PaymentResponse("Payment successfull via Card", "Payment successfull", true);

        Mockito.when(paymentService.processPayment(any(PaymentRequest.class))).thenReturn(response);

        String cardPaymentRequest = "{\"bookingId\":\"2123\",\"amount\":120.0,\"customerEmail\":\"customer@gmail.com\",\"paymentMethod\":\"Card\",\"details\":{\"cardNumber\":\"1234567812345678\",\"cardName\":\"abc\",\"expiry\":\"12/25\",\"cvv\":\"123\"}}"
;

        mockMvc.perform(post("/customer/pay")
                .header(HttpHeaders.AUTHORIZATION, "Bearer mockedToken")
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
        when(jwtUtility.isTokenValid(anyString(), any(UserDetails.class))).thenReturn(true);
        when(jwtUtility.extractUsername(anyString())).thenReturn("customer@gmail.com");
        when(jwtUtility.extractRole(anyString())).thenReturn("CUSTOMER");

        PaymentResponse response = new PaymentResponse("Payment successfull via Paypal", "Payment successfull", true);

        Mockito.when(paymentService.processPayment(any(PaymentRequest.class))).thenReturn(response);

        String cardPaymentRequest = "{\"bookingId\":\"2124\",\"amount\":120.0,\"customerEmail\":\"customer@gmail.com\",\"paymentMethod\":\"Paypal\",\"details\":{\"paypalID\":\"1234567812345678\"}}"
;

        mockMvc.perform(post("/customer/pay")
                .header(HttpHeaders.AUTHORIZATION, "Bearer mockedToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(cardPaymentRequest))
                .andExpect(status().isOk()) // Expect 200 OK
                .andExpect(jsonPath("$.result").value(true)) 
                .andExpect(jsonPath("$.status").value("Payment successfull")) 
                .andExpect(jsonPath("$.message").value("Payment successfull via Paypal")) 
                .andExpect(jsonPath("$.timeStamp").exists()); 
    }

    /// Test for verifying incorrect arguments
    @Test
    void testPaymentfailureArguments() throws Exception {

        when(jwtUtility.isTokenValid(anyString(), any(UserDetails.class))).thenReturn(true);
        when(jwtUtility.extractUsername(anyString())).thenReturn("customer@gmail.com");
        when(jwtUtility.extractRole(anyString())).thenReturn("CUSTOMER");

        PaymentResponse response = new PaymentResponse("Incorrect arguments", "Payment failure!", false);

        Mockito.when(paymentService.processPayment(any(PaymentRequest.class))).thenReturn(response);

        String cardPaymentRequest = "{\"bookingId\":\"2124\",\"amount\":120.0,\"customerEmail\":\"customer@gmail.com\",\"paymentMethod\":\"Card\",\"details\":{\"paypalID\":\"1234567812345678\"}}"
;

        mockMvc.perform(post("/customer/pay")
                .header(HttpHeaders.AUTHORIZATION, "Bearer mockedToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(cardPaymentRequest))
                .andExpect(status().isInternalServerError()) // Expect 500 INTERNAL SERVER ERROR
                .andExpect(jsonPath("$.result").value(false)) 
                .andExpect(jsonPath("$.status").value("Payment failure!")) 
                .andExpect(jsonPath("$.message").isNotEmpty()) 
                .andExpect(jsonPath("$.timeStamp").exists()); 
    }

    /// Method to test the card refund payment requests.
    @Test
    void testCardRefundEndpoint() throws Exception {
        when(jwtUtility.isTokenValid(anyString(), any(UserDetails.class))).thenReturn(true);
        when(jwtUtility.extractUsername(anyString())).thenReturn("customer@gmail.com");
        when(jwtUtility.extractRole(anyString())).thenReturn("CUSTOMER");

        PaymentResponse response = new PaymentResponse("Refund processed successfully via Card", "Refund processed successfully", true);

        Mockito.when(paymentService.refundPayment(any(String.class))).thenReturn(response);

        String cardRefundRequest = "{\"id\":\"2123\"}"
;

        mockMvc.perform(post("/customer/refund")
                .header(HttpHeaders.AUTHORIZATION, "Bearer mockedToken")
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
        when(jwtUtility.isTokenValid(anyString(), any(UserDetails.class))).thenReturn(true);
        when(jwtUtility.extractUsername(anyString())).thenReturn("customer@gmail.com");
        when(jwtUtility.extractRole(anyString())).thenReturn("CUSTOMER");

        PaymentResponse response = new PaymentResponse("Refund processed successfully via Paypal", "Refund processed successfully", true);

        Mockito.when(paymentService.refundPayment(any(String.class))).thenReturn(response);

        String paypalRefundRequest = "{\"id\":\"2124\"}"
;

        mockMvc.perform(post("/customer/refund")
                .header(HttpHeaders.AUTHORIZATION, "Bearer mockedToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(paypalRefundRequest))
                .andExpect(status().isOk()) // Expect 200 OK
                .andExpect(jsonPath("$.result").value(true)) 
                .andExpect(jsonPath("$.status").value("Refund processed successfully")) 
                .andExpect(jsonPath("$.message").value("Refund processed successfully via Paypal")) 
                .andExpect(jsonPath("$.timeStamp").exists()); 
    }

}


