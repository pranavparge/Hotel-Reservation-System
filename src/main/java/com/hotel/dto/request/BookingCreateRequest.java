package com.hotel.dto.request;

import lombok.Data;
import java.util.Date;
import java.util.List;
import com.hotel.room.entity.Room;
import jakarta.validation.constraints.NotNull;

@Data
public class BookingCreateRequest {
    @NotNull(message = "Customer ID is required")
    private Long customerID;
    @NotNull(message = "Room list cannot be null")
    private List<Room> totalRooms;
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
