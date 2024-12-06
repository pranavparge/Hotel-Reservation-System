package com.hotel.dto.request;

import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class RoomPriceRequest {
    @NotNull(message = "Start date is required")
    private String startDate;
}
