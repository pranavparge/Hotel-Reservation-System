package com.hotel;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotel.dto.request.*;
import com.hotel.dto.response.*;
import com.hotel.enums.ProgramType;
import com.hotel.repository.CustomerRepository;
import com.hotel.repository.StaffRepository;
import com.hotel.user.entity.Customer;
import com.hotel.user.entity.Staff;
import com.hotel.util.JwtUtility;
import com.hotel.util.TokenBlacklist;
import com.hotel.user.service.IAuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private IAuthService authService;

    @MockBean
    private JwtUtility jwtUtility;

    @MockBean
    private TokenBlacklist tokenBlacklist;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private CustomerRepository customerRepository;

    @MockBean
    private StaffRepository staffRepository;

    @BeforeEach
    public void setup() {
        InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager(
                User.withUsername("name@example.com")
                        .password("{noop}password")
                        .roles("CUSTOMER")
                        .build(),
                User.withUsername("testuser")
                        .password("{noop}testpassword")
                        .roles("USER")
                        .build()
        );

        Mockito.when(authService.userDetailsService()).thenReturn(inMemoryUserDetailsManager);
    }

    @BeforeEach
    public void setupMocks() {
        Mockito.when(jwtUtility.generateToken(any(), any())).thenReturn("mockedToken");
        Mockito.when(customerRepository.findFirstByEmail(any()))
                .thenReturn(Optional.of(new Customer(ProgramType.MEMBER, "Name", "email@example.com", "password")));
        Mockito.when(staffRepository.findFirstByEmail(any()))
                .thenReturn(Optional.of(new Staff("StaffName", "staff@example.com", "password")));
    }

    @Test
    public void testSignUpCustomer_Success() throws Exception {
        CustomerSignUpRequest request = new CustomerSignUpRequest("name@example.com", "password", "Name", ProgramType.MEMBER.toString());
        CustomerSignUpResponse response = new CustomerSignUpResponse(1L, ProgramType.MEMBER, "name@example.com", "Name");

        when(authService.createCustomer(any(CustomerSignUpRequest.class))).thenReturn(response);

        mockMvc.perform(post("/auth/customer/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value("1"))
                .andExpect(jsonPath("$.email").value("name@example.com"))
                .andExpect(jsonPath("$.name").value("Name"));
    }

    @Test
    public void testSignUpCustomer_EntityExists() throws Exception {
        CustomerSignUpRequest request = new CustomerSignUpRequest("name@example.com", "password", "Name", ProgramType.MEMBER.toString());

        when(authService.createCustomer(any(CustomerSignUpRequest.class)))
                .thenThrow(new jakarta.persistence.EntityExistsException("Customer already exists"));

        mockMvc.perform(post("/auth/customer/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Customer already exists"));
    }

    @Test
    public void testSignInCustomer_Success() throws Exception {
        CustomerSignInRequest request = new CustomerSignInRequest("name@example.com", "password");

        Customer customer = new Customer(ProgramType.MEMBER, "Name", "name@example.com", "password");
        Mockito.when(customerRepository.findFirstByEmail("name@example.com")).thenReturn(Optional.of(customer));

        Mockito.when(jwtUtility.generateToken(any(), any())).thenReturn("jwtToken");

        mockMvc.perform(post("/auth/customer/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jwt").value("jwtToken"))
                .andExpect(jsonPath("$.customerId").value(customer.getCustomerId()));
    }

    @Test
    public void testSignInCustomer_BadCredentials() throws Exception {
        CustomerSignInRequest request = new CustomerSignInRequest("name@example.com", "wrongPassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Incorrect username or password"));

        mockMvc.perform(post("/auth/customer/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Incorrect username or password"));
    }

    @Test
    public void testLogoutCustomer_Success() throws Exception {
        mockMvc.perform(post("/auth/customer/log-out")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer validToken"))
                .andExpect(status().isOk())
                .andExpect(content().string("Logged out successfully"));
    }

    @Test
    public void testLogoutCustomer_InvalidToken() throws Exception {
        mockMvc.perform(post("/auth/customer/log-out")
                        .header(HttpHeaders.AUTHORIZATION, "InvalidToken"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid token format"));
    }

    @Test
    public void testSignUpStaff_Success() throws Exception {
        StaffSignUpRequest request = new StaffSignUpRequest("staff@example.com", "password", "Staff");
        StaffSignUpResponse response = new StaffSignUpResponse(1L,  "staff@example.com", "Staff");

        when(authService.createStaff(any(StaffSignUpRequest.class))).thenReturn(response);

        mockMvc.perform(post("/auth/staff/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.staffId").value("1"))
                .andExpect(jsonPath("$.email").value("staff@example.com"))
                .andExpect(jsonPath("$.name").value("Staff"));
    }
}
