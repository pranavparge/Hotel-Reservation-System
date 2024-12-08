package com.hotel.payment.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

/// The Email notification observer implemented from the notification observer
/// Responsible to send email notifications about customer payments and refunds
@Component
public class EmailServiceObserver implements IObserver {
    /// Java mail sender package
    @Autowired
    private JavaMailSender mailSender;

    /// The email id used to send the email addresses.
    private static String emailAddress = "hotereservation7@gmail.com";

    /// The method responsible to send notifications
    @Override
    public void update(Payment paymentModel, String message) {
        System.out.println(paymentModel.customerEmail + " " + paymentModel.bookingId);
        System.out.println("Payment email sent to the customer: " + paymentModel.customerEmail + " For payment mode: "+paymentModel.paymentMethod.name());
        sendEmail(paymentModel.customerEmail, message);
    }

    /// The method responsible to carry out the task of sending the email
    /// Leverages Gmail SMTP service
    /// Additional information in application.properties
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
