package com.hotel.user.entity;

import lombok.Data;
import com.hotel.enums.ProgramType;

@Data
public abstract class Program {
    protected ProgramType programName;

    public Program(ProgramType programName) {
        this.programName = programName;
    }
}
