package com.hotel.room.controller;

import java.util.List;

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
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Invalid input: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Room not created!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/customer/rooms")
    public ResponseEntity<?> viewRooms() {
        try {
            List<RoomCreateResponse> response = roomService.viewRooms();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Unable to fetch rooms!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

