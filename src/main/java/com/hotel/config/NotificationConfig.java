package com.hotel.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.hotel.payment.notification_service.email_service.observers.EmailServiceObserver;
import com.hotel.payment.notification_service.email_service.observers.SmsServiceObserver;
import com.hotel.payment.notification_service.email_service.subjects.PaymentNotifier;

import jakarta.annotation.PostConstruct;

@Configuration
public class NotificationConfig {
    @Autowired
    private PaymentNotifier notifier;
    @Autowired 
    private EmailServiceObserver emailServiceObserver;
    @Autowired
    private SmsServiceObserver smsServiceObserver;

    @PostConstruct
    public void configureServices(){
        notifier.attach(emailServiceObserver);
        notifier.attach(smsServiceObserver);
        System.out.println("Notification services initalised");
    }
}

