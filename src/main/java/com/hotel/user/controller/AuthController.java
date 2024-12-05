package com.hotel.user.controller;

import com.hotel.dto.request.CustomerSignInRequest;
import com.hotel.dto.request.StaffSignInRequest;
import com.hotel.dto.response.CustomerSignInResponse;
import com.hotel.dto.response.StaffSignInResponse;
import com.hotel.repository.CustomerRepository;
import com.hotel.repository.StaffRepository;
import com.hotel.user.entity.Customer;
import com.hotel.user.entity.Staff;
import com.hotel.util.Error;
import com.hotel.util.JwtUtility;
import com.hotel.util.TokenBlacklist;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import jakarta.persistence.EntityExistsException;

import com.hotel.user.service.IAuthService;

import com.hotel.dto.request.StaffSignUpRequest;
import com.hotel.dto.response.StaffSignUpResponse;
import com.hotel.dto.request.CustomerSignUpRequest;
import com.hotel.dto.response.CustomerSignUpResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final IAuthService authService;
    private final AuthenticationManager authenticationManager;
    private final CustomerRepository customerRepository;
    private final StaffRepository staffRepository;
    private final JwtUtility jwtUtility;
    private final TokenBlacklist tokenBlacklist;

    @PostMapping("/customer/sign-up")
    public ResponseEntity<?> signUpCustomer(@Valid @RequestBody CustomerSignUpRequest request) {
        try {
            CustomerSignUpResponse newCustomer = authService.createCustomer(request);
            return new ResponseEntity<>(newCustomer, HttpStatus.OK);
        } catch (EntityExistsException entityExistsException){
            return new ResponseEntity<>(
                    new Error("Customer already exists!", entityExistsException.getMessage(), HttpStatus.BAD_REQUEST.value()),
                    HttpStatus.BAD_REQUEST
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new Error("Customer not created!", e.getMessage(), HttpStatus.BAD_REQUEST.value()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @PostMapping("/staff/sign-up")
    public ResponseEntity<?> signUpStaff(@Valid @RequestBody StaffSignUpRequest request) {
        try {
            StaffSignUpResponse newStaff = authService.createStaff(request);
            return new ResponseEntity<>(newStaff, HttpStatus.OK);
        } catch (EntityExistsException entityExistsException){
            return new ResponseEntity<>(
                    new Error("Staff already exists!", entityExistsException.getMessage(), HttpStatus.BAD_REQUEST.value()),
                    HttpStatus.BAD_REQUEST
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new Error("Staff not created!", e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @PostMapping("/customer/sign-in")
    public ResponseEntity<?> signInCustomer(@Valid @RequestBody CustomerSignInRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            final UserDetails userDetails = authService.userDetailsService().loadUserByUsername(request.getEmail());
            Optional<Customer> optionalCustomer = customerRepository.findFirstByEmail(userDetails.getUsername());
            if (optionalCustomer.isPresent()) {
                final String jwt = jwtUtility.generateToken(userDetails, "CUSTOMER");
                Customer customer = optionalCustomer.get();
                CustomerSignInResponse response = new CustomerSignInResponse();
                response.setJwt(jwt);
                response.setCustomerID(customer.getCustomerID());
                response.setProgramType(customer.getProgramType());
                response.setName(customer.getName());
                response.setEmail(customer.getEmail());
                return ResponseEntity.ok(response);
            } else {
                return new ResponseEntity<>(
                        new Error("Authentication Failed", "Customer not found", HttpStatus.UNAUTHORIZED.value()),
                        HttpStatus.UNAUTHORIZED
                );
            }
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(
                    new Error("Authentication Failed", "Incorrect username or password", HttpStatus.UNAUTHORIZED.value()),
                    HttpStatus.UNAUTHORIZED
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new Error("Sign-In Failed", e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @PostMapping("/staff/sign-in")
    public ResponseEntity<?> signInStaff(@Valid @RequestBody StaffSignInRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            final UserDetails userDetails = authService.userDetailsService().loadUserByUsername(request.getEmail());
            Optional<Staff> optionalStaff = staffRepository.findFirstByEmail(userDetails.getUsername());
            if (optionalStaff.isPresent()) {
                final String jwt = jwtUtility.generateToken(userDetails, "STAFF");
                Staff staff = optionalStaff.get();
                StaffSignInResponse response = new StaffSignInResponse();
                response.setJwt(jwt);
                response.setStaffID(staff.getStaffID());
                response.setName(staff.getName());
                response.setEmail(staff.getEmail());
                return ResponseEntity.ok(response);
            } else {
                return new ResponseEntity<>(
                        new Error("Authentication Failed", "Staff not found", HttpStatus.UNAUTHORIZED.value()),
                        HttpStatus.UNAUTHORIZED
                );
            }
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(
                    new Error("Authentication Failed", "Incorrect username or password", HttpStatus.UNAUTHORIZED.value()),
                    HttpStatus.UNAUTHORIZED
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new Error("Sign-In Failed", e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @PostMapping("/staff/log-out")
    public ResponseEntity<?> logoutStaff(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return new ResponseEntity<>(
                        new Error("Invalid Token", "Invalid token format", HttpStatus.BAD_REQUEST.value()),
                        HttpStatus.BAD_REQUEST
                );
            }
            String token = authHeader.substring(7);
            tokenBlacklist.add(token);
            return ResponseEntity.ok("Logged out successfully");
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new Error("Logout Failed", e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @PostMapping("/customer/log-out")
    public ResponseEntity<?> logoutCustomer(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return new ResponseEntity<>(
                        new Error("Invalid Token", "Invalid token format", HttpStatus.BAD_REQUEST.value()),
                        HttpStatus.BAD_REQUEST
                );
            }
            String token = authHeader.substring(7);
            tokenBlacklist.add(token);
            return ResponseEntity.ok("Logged out successfully");
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new Error("Logout Failed", e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}
