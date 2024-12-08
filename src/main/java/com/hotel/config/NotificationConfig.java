package com.hotel.config;

import com.hotel.payment.entity.EmailServiceObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.hotel.payment.entity.PaymentNotifier;

import jakarta.annotation.PostConstruct;

/// The config file to initalise the notification service
/// Intialisation of the [PaymentNotifier]
/// A [EmailServiceObserver] gets attached to this notifier which holds the [Payment]
/// Additional notification services can be attached and added.
@Configuration
public class NotificationConfig {
    @Autowired
    private PaymentNotifier notifier;
    @Autowired 
    private EmailServiceObserver emailServiceObserver;

    /// The method to configure all the services mentioned or required.
    @PostConstruct
    public void configureServices(){
        notifier.attach(emailServiceObserver);
        System.out.println("Notification services initalised");
    }
}
