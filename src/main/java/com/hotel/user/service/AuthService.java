package com.hotel.user.service;

import lombok.RequiredArgsConstructor;

import com.hotel.user.entity.Staff;
import com.hotel.enums.ProgramType;
import com.hotel.user.entity.Customer;

import jakarta.persistence.EntityExistsException;

import com.hotel.repository.StaffRepository;
import com.hotel.repository.CustomerRepository;
import com.hotel.dto.request.StaffSignUpRequest;
import com.hotel.dto.response.StaffSignUpResponse;
import com.hotel.dto.request.CustomerSignUpRequest;
import com.hotel.dto.response.CustomerSignUpResponse;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {
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
        Customer customer = new Customer(
                ProgramType.valueOf(request.getProgramType().toUpperCase()),
                request.getName(),
                request.getEmail(),
                new BCryptPasswordEncoder().encode(request.getPassword())
        );
        Customer newCustomer = customerRepository.save(customer);
        return newCustomer.getCustomerResponse();
    }

    @Override
    public UserDetailsService userDetailsService() {
        return username -> customerRepository.findFirstByEmail(username)
                .map(customer -> new org.springframework.security.core.userdetails.User(
                        customer.getEmail(),
                        customer.getPassword(),
                        List.of(new SimpleGrantedAuthority("CUSTOMER"))
                ))
                .or(() -> staffRepository.findFirstByEmail(username)
                        .map(staff -> new org.springframework.security.core.userdetails.User(
                                staff.getEmail(),
                                staff.getPassword(),
                                List.of(new SimpleGrantedAuthority("STAFF"))
                        )))
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));
    }
}
