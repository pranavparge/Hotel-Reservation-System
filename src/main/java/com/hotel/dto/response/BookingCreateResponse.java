package com.hotel.dto.response;

import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
public class BookingCreateResponse {
    private Long bookingID;
    private Long customerID;
    private List<String> additionalServices;
    private double basePrice;
    private Date startDate;
    private Date endDate;
    private int totalNumberOfGuests;
    private Date timeStamp;
}
