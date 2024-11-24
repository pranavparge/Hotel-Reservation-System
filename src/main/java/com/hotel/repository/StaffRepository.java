package com.hotel.repository;

import java.util.Optional;

import com.hotel.user.entity.Staff;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {
    Optional<Staff> findFirstByEmail(String email);
}
