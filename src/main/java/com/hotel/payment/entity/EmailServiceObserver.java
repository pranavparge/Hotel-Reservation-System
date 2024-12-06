package com.hotel.payment.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailServiceObserver implements Observer{
    @Autowired
    private JavaMailSender mailSender;

    private static String emailAddress = "hotereservation7@gmail.com";

    @Override
    public void update(Payment paymentModel, String message) {
        System.out.println(paymentModel.customerEmail + " " + paymentModel.bookingID);
        System.out.println("Payment email sent to the customer: " + paymentModel.customerEmail + " For payment mode: "+paymentModel.paymentMethod.name());
        sendEmail(paymentModel.customerEmail, message);
    }

    private void sendEmail(String email, String msg){
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Payment info");
            message.setText(msg);
            message.setFrom(emailAddress);

            mailSender.send(message);
        } catch (Exception e) {
            System.out.println("Email not send: "+e.getMessage());
        }
    }
}
