package com.hotel.dto.response;

import com.hotel.enums.ProgramType;
import lombok.Data;

@Data
public class CustomerSignUpResponse {
    private Long customerId;
    private ProgramType programType;
    private String email;
    private String name;
}
