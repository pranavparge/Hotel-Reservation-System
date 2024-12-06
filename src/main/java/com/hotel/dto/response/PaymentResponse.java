package com.hotel.dto.response;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;

@Data
public class PaymentResponse {
    private boolean result;
    private String status;
    private String message;
    @CreationTimestamp
    private Instant timeStamp;


    public PaymentResponse(String message, String status, boolean result){
        this.result = result;
        this.message = message;
        this.status = status;
        this.timeStamp = Instant.now();
    }

    public boolean getResult(){
        return this.result;
    }
    public String getStatus(){
        return this.status;
    }
}
