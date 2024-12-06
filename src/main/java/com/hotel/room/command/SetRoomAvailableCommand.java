package com.hotel.room.command;

import com.hotel.room.entity.Room;

public class SetRoomAvailableCommand implements RoomCommand {
    private final Room room;

    public SetRoomAvailableCommand(Room room) {
        this.room = room;
    }

    @Override
    public void execute() {
        room.setStatus("Available");
    }
}