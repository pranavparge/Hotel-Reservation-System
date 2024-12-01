package com.hotel.payment.email_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.hotel.payment.email_service.observers.EmailServiceObserver;
import com.hotel.payment.email_service.subjects.PaymentNotifier;

import jakarta.annotation.PostConstruct;


@Configuration
public class ObserverConfig {

    @Autowired
    private PaymentNotifier paymentNotifier;

    @Autowired
    private EmailServiceObserver emailService;

    @PostConstruct
    public void configureObservers() {
        paymentNotifier.attach(emailService);
    }
}