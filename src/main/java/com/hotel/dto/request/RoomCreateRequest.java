package com.hotel.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RoomCreateRequest {
    @NotBlank(message = "Room number is required")
    private String roomNumber;

    @NotNull(message = "Room capacity is required")
    private Integer roomCapacity;

    @NotBlank(message = "Room type is required")
    private String roomType;

    @NotBlank(message = "Room status is required")
    private String roomStatus;
}
