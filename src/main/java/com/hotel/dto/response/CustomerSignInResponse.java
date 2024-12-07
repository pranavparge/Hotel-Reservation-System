package com.hotel.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import com.hotel.enums.ProgramType;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerSignInResponse {
    private Long customerId;
    private ProgramType programType;
    private String email;
    private String name;
    private String jwt;
}
