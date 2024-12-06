package com.hotel.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RefundPaymentRequest {
    @NotNull(message = "Name is required")
    private String name;

    @NotNull(message = "BookingID is required")
    private String bookingId;
}