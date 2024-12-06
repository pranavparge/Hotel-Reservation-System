package com.hotel.dto.response;

import com.hotel.enums.RoomStatus;
import lombok.Data;
import com.hotel.enums.RoomType;

@Data
public class RoomCreateResponse {
    private String roomNumber;
    private int roomCapacity;
    private RoomType roomType;
    private Double roomPrice;
    private RoomStatus roomStatus;
}
