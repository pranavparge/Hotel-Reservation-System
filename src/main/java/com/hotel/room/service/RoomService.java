package com.hotel.room.service;

import java.util.List;
import java.util.stream.Collectors;

import com.hotel.dto.request.RoomPriceRequest;
import com.hotel.dto.response.RoomPriceResponse;
import com.hotel.room.command.RoomStatusInvoker;
import com.hotel.room.command.SetRoomAvailableCommand;
import com.hotel.room.command.SetRoomOccupiedCommand;
import com.hotel.room.command.SetRoomUnavailableCommand;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import com.hotel.room.entity.*;
import com.hotel.repository.RoomRepository;
import com.hotel.dto.request.RoomCreateRequest;
import com.hotel.dto.response.RoomCreateResponse;

import org.json.JSONObject;
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
                price
        );
        Room newRoom = roomRepository.save(room);
        return newRoom.getRoomResponse();
    }

    @Override
    @Transactional
    public RoomPriceResponse getRoomPrice(RoomPriceRequest roomPriceRequest) {
        Integer singleRoomBooked = roomRepository.getRoomNotAvailable(roomPriceRequest.getStartDate(), "SINGLE");
        Integer doubleRoomBooked = roomRepository.getRoomNotAvailable(roomPriceRequest.getStartDate(), "DOUBLE");
        Integer suiteRoomBooked = roomRepository.getRoomNotAvailable(roomPriceRequest.getStartDate(), "SUITE");

        JSONObject jsonObject = new JSONObject();

        Price singleRoom = new SingleRoomPrice();
        Price doubleRoom = new DoubleRoomPrice();
        Price suiteRoom = new SuiteRoomPrice();

        double singlePrice = 0.0 , doublePrice = 0.0 , suitePrice = 0.0;

        Integer roomSingle = roomRepository.getRoomByType("SINGLE");
        Integer roomDouble = roomRepository.getRoomByType("DOUBLE");
        Integer roomSuite = roomRepository.getRoomByType("SUITE");

        Room visitor = new Room();
        singleRoom.accept(visitor, roomSingle, singleRoomBooked );
        singlePrice = visitor.getFinalPrice();
        doubleRoom.accept(visitor, roomDouble, doubleRoomBooked);
        doublePrice = visitor.getFinalPrice();
        suiteRoom.accept(visitor, roomSuite, suiteRoomBooked);
        suitePrice = visitor.getFinalPrice();

        RoomPriceResponse roomPriceResponse = new RoomPriceResponse();
        roomPriceResponse.setSingleRoomPrice(singlePrice);
        roomPriceResponse.setDoubleRoomPrice(doublePrice);
        roomPriceResponse.setSuiteRoomPrice(suitePrice);

        return roomPriceResponse;
    }

    /**
     * Update the status of a room.
     *
     * @param roomNumber the room number
     * @param status     the new status
     */
    public void updateRoomStatus(String roomNumber, String status) {
        // Find the room by room number
        Room room = roomRepository.findByRoomNumber(roomNumber)
                .orElseThrow(() -> new IllegalArgumentException("Room not found with number: " + roomNumber));

        // Initialize the invoker
        RoomStatusInvoker invoker = new RoomStatusInvoker();

        // Select the appropriate command based on status
        switch (status.toUpperCase()) {
            case "AVAILABLE" -> invoker.setCommand(new SetRoomAvailableCommand(room));
            case "UNAVAILABLE" -> invoker.setCommand(new SetRoomUnavailableCommand(room));
            case "OCCUPIED" -> invoker.setCommand(new SetRoomOccupiedCommand(room));
            default -> throw new IllegalArgumentException("Invalid room status: " + status);
        }

        // Execute the command
        invoker.executeCommand();

        // Save the updated room entity
        roomRepository.save(room);
    }
}
