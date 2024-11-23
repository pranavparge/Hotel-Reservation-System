package com.hotel.user.service.auth;

import com.hotel.dto.request.StaffSignUpRequest;
import com.hotel.dto.response.StaffSignUpResponse;
import com.hotel.dto.request.CustomerSignUpRequest;
import com.hotel.dto.response.CustomerSignUpResponse;

public interface AuthService {
    CustomerSignUpResponse createCustomer(CustomerSignUpRequest request);
    StaffSignUpResponse createStaff(StaffSignUpRequest request);
}
