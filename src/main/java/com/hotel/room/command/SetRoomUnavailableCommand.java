package com.hotel.room.command;

import com.hotel.room.entity.Room;

public class SetRoomUnavailableCommand implements RoomCommand {
    private final Room room;

    public SetRoomUnavailableCommand(Room room) {
        this.room = room;
    }

    @Override
    public void execute() {
        room.setStatus("Unavailable");
    }
}