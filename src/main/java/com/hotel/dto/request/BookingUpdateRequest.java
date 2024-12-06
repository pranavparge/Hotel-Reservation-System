package com.hotel.dto.request;

import lombok.Data;
import com.hotel.enums.RoomType;
import jakarta.validation.constraints.NotNull;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

@Data
public class BookingUpdateRequest {
    @NotNull(message = "Start date is required")
    private Date startDate;

    @NotNull(message = "End date is required")
    private Date endDate;

    @NotNull(message = "Total number of guests is required")
    private int totalNumberOfGuests;

    private List<String> additionalServices = new ArrayList<>();

    @NotNull(message = "Room type and quantity map cannot be null")
    private Map<RoomType, Integer> roomTypeToQuantity;
}
