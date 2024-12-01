package com.hotel.user.entity;

import com.hotel.enums.ProgramType;

public class Loyalty extends Program {
    public Loyalty() {
        super(ProgramType.LOYALTY);
    }

    @Override
    public String getProgramBenefits() {
        return "Earn and redeem loyalty points";
    }
}
