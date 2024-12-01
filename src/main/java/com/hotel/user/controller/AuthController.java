package com.hotel.user.controller;

import com.hotel.dto.request.CustomerSignInRequest;
import com.hotel.dto.request.StaffSignInRequest;
import com.hotel.dto.response.CustomerSignInResponse;
import com.hotel.dto.response.StaffSignInResponse;
import com.hotel.repository.CustomerRepository;
import com.hotel.repository.StaffRepository;
import com.hotel.user.entity.Customer;
import com.hotel.user.entity.Staff;
import com.hotel.util.JwtUtility;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/customer/sign-up")
    public ResponseEntity<?> signUpCustomer(@Valid @RequestBody CustomerSignUpRequest request) {
        try {
            CustomerSignUpResponse newCustomer = authService.createCustomer(request);
            return new ResponseEntity<>(newCustomer, HttpStatus.OK);
        } catch (EntityExistsException entityExistsException){
            return new ResponseEntity<>("Customer already exists!", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Customer not created!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/staff/sign-up")
    public ResponseEntity<?> signUpStaff(@Valid @RequestBody StaffSignUpRequest request) {
        try {
            StaffSignUpResponse newStaff = authService.createStaff(request);
            return new ResponseEntity<>(newStaff, HttpStatus.OK);
        } catch (EntityExistsException entityExistsException){
            return new ResponseEntity<>("Staff already exists!", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Staff not created!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/customer/sign-in")
    public CustomerSignInResponse signInCustomer(@Valid @RequestBody CustomerSignInRequest request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        } catch (BadCredentialsException e){
            throw new BadCredentialsException("Incorrect Username or Password");
        }
        final UserDetails userDetails = authService.userDetailsService().loadUserByUsername(request.getEmail());
        Optional<Customer> optionalCustomer = customerRepository.findFirstByEmail(userDetails.getUsername());
        final String jwt = jwtUtility.generateToken(userDetails, "CUSTOMER");
        CustomerSignInResponse customerSignInResponse = new CustomerSignInResponse();
        if(optionalCustomer.isPresent()) {
            customerSignInResponse.setJwt(jwt);
            customerSignInResponse.setCustomerID(optionalCustomer.get().getCustomerID());
            customerSignInResponse.setProgramType(optionalCustomer.get().getProgramType());
            customerSignInResponse.setName(optionalCustomer.get().getName());
            customerSignInResponse.setEmail(optionalCustomer.get().getEmail());
        }
        return customerSignInResponse;
    }

    @PostMapping("/staff/sign-in")
    public StaffSignInResponse signInStaff(@Valid @RequestBody StaffSignInRequest request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        } catch (BadCredentialsException e){
            throw new BadCredentialsException("Incorrect Username or Password");
        }
        final UserDetails userDetails = authService.userDetailsService().loadUserByUsername(request.getEmail());
        Optional<Staff> optionalStaff = staffRepository.findFirstByEmail(userDetails.getUsername());
        final String jwt = jwtUtility.generateToken(userDetails, "STAFF");
        StaffSignInResponse staffSignInResponse = new StaffSignInResponse();
        if(optionalStaff.isPresent()) {
            staffSignInResponse.setJwt(jwt);
            staffSignInResponse.setStaffID(optionalStaff.get().getStaffID());
            staffSignInResponse.setName(optionalStaff.get().getName());
            staffSignInResponse.setEmail(optionalStaff.get().getEmail());
        }
        return staffSignInResponse;
    }
}
