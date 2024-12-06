package com.hotel.dto.response;

import com.hotel.enums.ProgramType;
import lombok.Data;

@Data
public class StaffSignInResponse {
    private Long staffId;
    private String email;
    private String name;
    private String jwt;
}
