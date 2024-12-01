package com.hotel.dto.response;

import com.hotel.enums.ProgramType;
import lombok.Data;

@Data
public class StaffSignInResponse {
    private Long staffID;
    private String email;
    private String name;
    private String jwt;
}
