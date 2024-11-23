package com.hotel.user.controller.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.persistence.EntityExistsException;
import com.hotel.user.service.auth.AuthService;
import com.hotel.dto.request.StaffSignUpRequest;
import com.hotel.dto.response.StaffSignUpResponse;
import com.hotel.dto.request.CustomerSignUpRequest;
import com.hotel.dto.response.CustomerSignUpResponse;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/customer/sign-up")
    public ResponseEntity<?> signUpCustomer(@Valid @RequestBody CustomerSignUpRequest request) {
        try {
            CustomerSignUpResponse newCustomer = authService.createCustomer(request);
            return new ResponseEntity<>(newCustomer, HttpStatus.OK);
        } catch (EntityExistsException entityExistsException){
            return new ResponseEntity<>("Customer already exists!", HttpStatus.NOT_ACCEPTABLE);
        } catch (Exception e) {
            return new ResponseEntity<>("Customer not created!", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/staff/sign-up")
    public ResponseEntity<?> signUpStaff(@Valid @RequestBody StaffSignUpRequest request) {
        try {
            StaffSignUpResponse newStaff = authService.createStaff(request);
            return new ResponseEntity<>(newStaff, HttpStatus.OK);
        } catch (EntityExistsException entityExistsException){
            return new ResponseEntity<>("Staff already exists!", HttpStatus.NOT_ACCEPTABLE);
        } catch (Exception e) {
            return new ResponseEntity<>("Staff not created!", HttpStatus.BAD_REQUEST);
        }
    }
}
