package com.hotel.dto.response;

import com.hotel.enums.ProgramType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerSignUpResponse {
    private Long customerId;
    private ProgramType programType;
    private String email;
    private String name;
}
