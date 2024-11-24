package com.hotel.room.service;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

import com.hotel.room.entity.*;
import com.hotel.enums.RoomStatus;
import com.hotel.repository.RoomRepository;
import com.hotel.dto.request.RoomCreateRequest;
import com.hotel.dto.response.RoomCreateResponse;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomService implements IRoomService {
    private final RoomRepository roomRepository;

    @Override
    public List<RoomCreateResponse> viewRooms() {
        return roomRepository.findAll()
                .stream()
                .map(Room::getRoomResponse)
                .collect(Collectors.toList());
    }

    @Override
    public RoomCreateResponse createRoom(RoomCreateRequest request) {
        if (roomRepository.findByRoomNumber(request.getRoomNumber()).isPresent()) {
            throw new IllegalArgumentException("Room Already Exists With Given Room Number: " + request.getRoomNumber());
        }
        Price price = switch (request.getRoomType().toUpperCase()) {
            case "SINGLE" -> new SingleRoomPrice();
            case "DOUBLE" -> new DoubleRoomPrice();
            case "SUITE" -> new SuiteRoomPrice();
            default -> throw new IllegalArgumentException("Invalid room type: " + request.getRoomType());
        };
        Room room = new Room(
                request.getRoomNumber(),
                request.getRoomCapacity(),
                price,
                RoomStatus.valueOf(request.getRoomStatus().toUpperCase())
        );
        Room newRoom = roomRepository.save(room);
        return newRoom.getRoomResponse();
    }
}


