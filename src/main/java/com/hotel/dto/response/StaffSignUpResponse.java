package com.hotel.dto.response;

import lombok.Data;

@Data
public class StaffSignUpResponse {
    private Long staffId;
    private String email;
    private String name;
}
