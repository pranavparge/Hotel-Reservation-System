package com.hotel.dto.request;

import lombok.Data;
import java.util.Map;
import java.util.Date;

import com.hotel.enums.RoomType;
import jakarta.validation.constraints.NotNull;

@Data
public class BookingCreateRequest {
    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @NotNull(message = "Room type and quantity map cannot be null")
    private Map<RoomType, Integer> roomTypeToQuantity;

    @NotNull(message = "Start date is required")
    private Date startDate;

    @NotNull(message = "End date is required")
    private Date endDate;

    @NotNull(message = "Total number of guests is required")
    private int totalNumberOfGuests;

    private boolean addBreakfast;
    private boolean addLunch;
    private boolean addDinner;
}
