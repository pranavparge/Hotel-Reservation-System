package com.hotel.user.service.auth;

import lombok.RequiredArgsConstructor;
import com.hotel.user.entity.Staff;
import com.hotel.user.entity.Customer;
import org.springframework.stereotype.Service;
import com.hotel.enums.CustomerType;
import jakarta.persistence.EntityExistsException;
import com.hotel.user.entity.CustomerFactory;
import com.hotel.repository.StaffRepository;
import com.hotel.repository.CustomerRepository;
import com.hotel.dto.request.StaffSignUpRequest;
import com.hotel.dto.response.StaffSignUpResponse;
import com.hotel.dto.request.CustomerSignUpRequest;
import com.hotel.dto.response.CustomerSignUpResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final CustomerRepository customerRepository;
    private final StaffRepository staffRepository;

    @Override
    public StaffSignUpResponse createStaff(StaffSignUpRequest request) {
        if(staffRepository.findFirstByEmail(request.getEmail()).isPresent()) {
            throw new EntityExistsException("Staff Already Exists With Given Email " + request.getEmail());
        }
        Staff staff = new Staff(
                    request.getName(),
                    request.getEmail(),
                    new BCryptPasswordEncoder().encode(request.getPassword())
        );
        staffRepository.save(staff);
        Staff newStaff = staffRepository.save(staff);
        return newStaff.getStaffResponse();
    }

    @Override
    public CustomerSignUpResponse createCustomer(CustomerSignUpRequest request) {
        if(customerRepository.findFirstByEmail(request.getEmail()).isPresent()) {
            throw new EntityExistsException("Customer Already Exists With Given Email " + request.getEmail());
        }
        Customer customer = CustomerFactory.createCustomer(
                CustomerType.valueOf(request.getCustomerType().toUpperCase()),
                request.getName(),
                request.getEmail(),
                new BCryptPasswordEncoder().encode(request.getPassword())
        );
        Customer newCustomer = customerRepository.save(customer);
        return newCustomer.getCustomerResponse();
    }
}
