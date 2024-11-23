package com.hotel.repository;

import java.util.Optional;
import com.hotel.user.entity.Customer;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findFirstByEmail(String email);
}
