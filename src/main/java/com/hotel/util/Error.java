package com.hotel.util;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Error {
    private String error;
    private String message;
    private int statusCode;
    private LocalDateTime timestamp;

    public Error(String error, String message, int statusCode) {
        this.error = error;
        this.message = message;
        this.statusCode = statusCode;
        this.timestamp = LocalDateTime.now();
    }
}
