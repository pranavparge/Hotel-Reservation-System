package com.hotel.room.service;

import java.util.List;

import com.hotel.dto.request.RoomCreateRequest;
import com.hotel.dto.response.RoomCreateResponse;

public interface IRoomService {
    List<RoomCreateResponse> viewRooms();
    RoomCreateResponse createRoom(RoomCreateRequest request);
}
