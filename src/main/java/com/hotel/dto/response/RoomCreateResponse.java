package com.hotel.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import com.hotel.enums.RoomType;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomCreateResponse {
    private String roomNumber;
    private int roomCapacity;
    private RoomType roomType;
    private Double roomPrice;
}
