package com.hotel.dto.request;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class CustomerSignInRequest {
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;
}
