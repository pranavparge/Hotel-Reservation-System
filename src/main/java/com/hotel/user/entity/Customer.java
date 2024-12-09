package com.hotel.user.entity;

import jakarta.persistence.*;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.hotel.enums.ProgramType;
import com.hotel.dto.response.CustomerSignUpResponse;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class Customer extends User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;
    @Enumerated(EnumType.STRING)
    private ProgramType programType;
    @Transient
    private Program program;

    public Customer() {}

    public Customer(ProgramType programType, String name, String email, String password) {
        super(name, email, password);
        this.programType = programType;
        this.program = ProgramFactory.createProgram(programType);
    }

    @PostLoad
    private void loadProgram() {
        this.program = ProgramFactory.createProgram(this.programType);
    }

    public CustomerSignUpResponse getCustomerResponse() {
        CustomerSignUpResponse response = new CustomerSignUpResponse();
        response.setCustomerId(customerId);
        response.setProgramType(programType);
        response.setName(getName());
        response.setEmail(getEmail());
        return response;
    }
}
