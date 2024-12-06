package com.hotel.room.controller;

import java.util.List;

import com.hotel.dto.request.RoomPriceRequest;
import com.hotel.dto.response.RoomPriceResponse;
import com.hotel.util.Error;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import com.hotel.room.service.IRoomService;
import com.hotel.dto.request.RoomCreateRequest;
import com.hotel.dto.response.RoomCreateResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class RoomController {
    private final IRoomService roomService;

    @PostMapping("/staff/rooms/create")
    public ResponseEntity<?> createRoom(@Valid @RequestBody RoomCreateRequest request) {
        try {
            RoomCreateResponse newRoom = roomService.createRoom(request);
            return new ResponseEntity<>(newRoom, HttpStatus.OK);
        } catch (IllegalArgumentException illegalArgumentException) {
            return new ResponseEntity<>(
                    new Error("Invalid input!", illegalArgumentException.getMessage(), HttpStatus.BAD_REQUEST.value()),
                    HttpStatus.BAD_REQUEST
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new Error("Room not created!", e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping("/customer/rooms")
    public ResponseEntity<?> viewRooms() {
        try {
            List<RoomCreateResponse> response = roomService.viewRooms();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new Error("Unable to fetch rooms!", e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @PostMapping("/customer/rooms/price")
    public ResponseEntity<?> getRoomPrice(@Valid @RequestBody RoomPriceRequest roomPriceRequest) {
        try {
            RoomPriceResponse roomPriceResponse = roomService.getRoomPrice(roomPriceRequest);
            return ResponseEntity.ok(roomPriceResponse);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid input: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + e.getMessage());
        }
    }

    // New Endpoint: Update Room Status
    @PostMapping("/staff/rooms/{roomNumber}/status")
    public ResponseEntity<?> updateRoomStatus(
            @PathVariable String roomNumber,
            @RequestBody String status) {
        try {
            roomService.updateRoomStatus(roomNumber, status); // Call service to update status
            return new ResponseEntity<>("Room status updated to: " + status, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(
                    new Error("Invalid input!", e.getMessage(), HttpStatus.BAD_REQUEST.value()),
                    HttpStatus.BAD_REQUEST
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new Error("Unable to update room status!", e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}

