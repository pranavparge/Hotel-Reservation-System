package com.hotel.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefundPaymentRequest {
    @NotBlank(message = "CustomerId is required")
    private String customerId;

    @NotBlank(message = "BookingID is required")
    private String bookingId;

    public String getBookingID(){
        return this.bookingId;
    }
    public String getCustomerID(){
        return this.customerId;
    }
}