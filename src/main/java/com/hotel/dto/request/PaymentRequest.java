package com.hotel.dto.request;

import java.util.Map;
import java.util.regex.Pattern;

import com.hotel.enums.PaymentMethod;
import com.hotel.payment.entity.CardPaymentDetails;
import com.hotel.payment.entity.IPaymentDetails;
import com.hotel.payment.entity.PaymentDetailsFactory;
import com.hotel.payment.entity.PaypalPaymentDetails;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/// Example request packet
// {
//     "bookingID":,
//     "customerEmail":
//     "amount":,
//     "paymentMethod",
//     "details": {},
// }

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {
    @NotBlank(message = "Booking ID is required")
    private String bookingId;
    @NotBlank(message =  "Email is required")
    private String customerEmail;
    @NotBlank(message = "Amount of the booking is required")
    private double amount;
    @NotBlank(message = "Payment method is required")
    @Enumerated
    private PaymentMethod paymentMethod;
    @Enumerated
    @NotBlank(message = "Payment details are required")
    private IPaymentDetails paymentDetails;

    private static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";

    // private IPaymentDetails setPaymentDetails(Map<String, Object> data, PaymentMethod method) {
    //     switch (method) {
    //         case CARD:
    //             return new CardPaymentDetails(data); // Constructor for CardPaymentDetails
    //         case PAYPAL:
    //             return new PaypalPaymentDetails(data); // Constructor for PaypalPaymentDetails
    //         default:
    //             throw new IllegalArgumentException("Unsupported payment method: " + method);
    //     }
    // }

    // private PaymentMethod setPaymentMethod(String method){
    //     switch(method){
    //         case "Card":
    //             return PaymentMethod.CARD;
    //         case "Paypal":
    //             return PaymentMethod.PAYPAL;
    //         default:
    //             return PaymentMethod.CARD;        
    //     }
    // }

    @SuppressWarnings("unchecked")
    public PaymentRequest(PaymentDetailsFactory paymentDetailsFactory, Map<String, Object> data){
        this.bookingId = data.get("bookingId").toString();
        this.customerEmail = data.get("customerEmail").toString();
        this.amount = Double.parseDouble(data.get("amount").toString());
        this.paymentMethod = paymentDetailsFactory.getPaymentMethod(data.get("paymentMethod").toString());
        this.paymentDetails = paymentDetailsFactory.getPaymentDetails((Map<String, Object>) data.get("details") , paymentDetailsFactory.getPaymentMethod(data.get("paymentMethod").toString()));
    }

     public double getAmount(){
         return this.amount;
     }

     public String getbookingId(){
         return this.bookingId;
     }

     public IPaymentDetails getPaymentDetails(){
         return this.paymentDetails;
     }

     public String getEmail(){
         return this.customerEmail;
     }

    public String validate(){
        if(amount <= 0){
            return "Amount cannot be smaller than or equal to zero!";
        }
        else if(!Pattern.matches(EMAIL_REGEX, customerEmail)){
            return "Provide a valid email";
        }
        return "";
    }
}

