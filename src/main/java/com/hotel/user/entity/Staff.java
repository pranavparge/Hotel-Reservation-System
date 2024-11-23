package com.hotel.user.entity;

import lombok.Data;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import com.hotel.dto.response.StaffSignUpResponse;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class Staff extends User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long staffID;

    public Staff() {}

    public Staff(String name, String email, String password) {
        super(name, email, password);
    }

    public StaffSignUpResponse getStaffResponse() {
        StaffSignUpResponse response = new StaffSignUpResponse();
        response.setStaffID(staffID);
        response.setName(getName());
        response.setEmail(getEmail());
        return response;
    }
}
