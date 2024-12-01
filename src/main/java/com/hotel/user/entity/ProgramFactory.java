package com.hotel.user.entity;

import com.hotel.enums.ProgramType;

public class ProgramFactory {
    public static Program createProgram(ProgramType programType) {
        return switch (programType) {
            case MEMBER -> new Member();
            case LOYALTY -> new Loyalty();
            default -> throw new IllegalArgumentException("Invalid customer type: " + programType);
        };
    }
}
