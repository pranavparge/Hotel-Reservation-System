package com.hotel.user.entity;

import com.hotel.enums.ProgramType;
import lombok.Data;

@Data
public abstract class Program {
    protected ProgramType programName;

    public Program(ProgramType programName) {
        this.programName = programName;
    }

    public abstract String getProgramBenefits();
}
