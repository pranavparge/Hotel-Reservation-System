package com.hotel.room.command;

import com.hotel.room.entity.Room;

public class SetRoomOccupiedCommand implements RoomCommand {
    private final Room room;

    public SetRoomOccupiedCommand(Room room) {
        this.room = room;
    }

    @Override
    public void execute() {
        room.setStatus("Occupied");
    }
}