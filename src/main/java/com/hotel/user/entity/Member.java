package com.hotel.user.entity;

import com.hotel.enums.ProgramType;

public class Member extends Program {
    public Member() {
        super(ProgramType.MEMBER);
    }

    @Override
    public String getProgramBenefits() {
        return "Access to member benefits and discounts.";
    }
}
