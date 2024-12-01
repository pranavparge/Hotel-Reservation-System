package com.hotel.dto.response;

import com.hotel.enums.ProgramType;

import lombok.Data;

@Data
public class CustomerSignUpResponse {
    private Long customerID;
    private String email;
    private String name;
    private ProgramType programType;
}
