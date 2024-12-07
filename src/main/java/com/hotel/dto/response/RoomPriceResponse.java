package com.hotel.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomPriceResponse {
    private double singleRoomPrice;
    private double doubleRoomPrice;
    private double suiteRoomPrice;
}
