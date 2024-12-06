package com.hotel.room.service;

import java.util.List;

import com.hotel.dto.request.RoomCreateRequest;
import com.hotel.dto.request.RoomPriceRequest;
import com.hotel.dto.response.RoomCreateResponse;
import com.hotel.dto.response.RoomPriceResponse;
import com.hotel.enums.RoomStatus;
import jakarta.transaction.Transactional;

public interface IRoomService {
    List<RoomCreateResponse> viewRooms();
    RoomCreateResponse createRoom(RoomCreateRequest request);
    RoomPriceResponse getRoomPrice(RoomPriceRequest roomPriceRequest);
    void updateRoomStatus(String roomNumber, RoomStatus status);
}
