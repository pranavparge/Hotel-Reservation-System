package com.hotel.room.entity;

public interface IRoomVisitor {

    void visit(SingleRoomPrice singleRoomPrice, double totalRoom, double roomBooked);
    void visit(DoubleRoomPrice doubleRoomPrice, double totalRoom, double roomBooked);
    void visit(SuiteRoomPrice suiteRoomPrice, double totalRoom, double roomBooked);
}
