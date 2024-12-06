package com.hotel.room.command;

public class RoomStatusInvoker {
    private RoomCommand command;

    public void setCommand(RoomCommand command) {
        this.command = command;
    }

    public void executeCommand() {
        if (command != null) {
            command.execute();
        }
    }
}