package com.hotel.config;

import com.hotel.payment.entity.EmailServiceObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.hotel.payment.entity.SmsServiceObserver;
import com.hotel.payment.entity.PaymentNotifier;

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
