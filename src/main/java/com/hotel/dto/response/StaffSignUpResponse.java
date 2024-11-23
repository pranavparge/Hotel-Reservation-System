package com.hotel.dto.response;

import lombok.Data;

@Data
public class StaffSignUpResponse {
    private Long staffID;
    private String email;
    private String name;
}
