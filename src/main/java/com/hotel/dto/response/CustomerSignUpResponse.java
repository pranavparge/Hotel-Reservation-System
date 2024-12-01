package com.hotel.dto.response;

import lombok.Data;
import com.hotel.enums.ProgramType;

@Data
public class CustomerSignUpResponse {
    private Long customerID;
    private ProgramType programType;
    private String email;
    private String name;
}
