package com.hotel.dto.request;

import com.hotel.enums.RoomType;
import com.hotel.room.entity.Room;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
