package com.hotel.payment.factory;

import com.hotel.enums.PaymentMethod;
import com.hotel.payment.models.CardPaymentModel;
import com.hotel.payment.models.PaymentModel;
import com.hotel.payment.models.PaypalPaymentModel;
import com.hotel.payment.models.payment_details.PaymentDetails;

public class PaymentFactory {
    public static PaymentModel createPayment(double amount, String bookingID, PaymentDetails details){
        return details.createPaymentModel(amount, bookingID);
    }
}
