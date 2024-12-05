package com.hotel.room.entity;

public interface RoomVisitor {

    void visit(SingleRoomPrice singleRoomPrice, double totalRoom, double roomBooked);
    void visit(DoubleRoomPrice doubleRoomPrice, double totalRoom, double roomBooked);
    void visit(SuiteRoomPrice suiteRoomPrice, double totalRoom, double roomBooked);
}
