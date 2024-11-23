package com.hotel.dto.response;

import lombok.Data;

@Data
public class CustomerSignUpResponse {
    private Long customerID;
    private String email;
    private String name;
}
