package com.hotel.dto.response;

import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
public class BookingCreateResponse {
    private Long bookingId;
    private Long customerId;
    private List<String> additionalServices;
    private double totalPrice;
    private Date startDate;
    private Date endDate;
    private int totalNumberOfGuests;
    private Date timeStamp;
    private List<RoomCreateResponse> totalRooms;
}
