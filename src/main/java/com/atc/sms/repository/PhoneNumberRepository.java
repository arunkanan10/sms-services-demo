package com.atc.sms.repository;

import com.atc.sms.model.PhoneNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhoneNumberRepository extends JpaRepository<PhoneNumber, Long> {
    PhoneNumber findByNumber(String phoneNumber);
}
