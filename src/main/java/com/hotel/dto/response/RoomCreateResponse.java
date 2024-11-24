package com.hotel.dto.response;

import lombok.Data;
import com.hotel.enums.RoomType;
import com.hotel.enums.RoomStatus;

@Data
public class RoomCreateResponse {
    private String roomNumber;
    private int roomCapacity;
    private RoomType roomType;
    private Double roomPrice;
    private RoomStatus roomStatus;
}
