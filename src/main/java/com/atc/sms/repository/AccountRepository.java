package com.atc.sms.repository;

import com.atc.sms.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query("SELECT acc FROM Account acc WHERE acc.authId = :authId AND acc.userName = :userName")
    Account authenticate(@Param("authId") String authId, @Param("userName") String userName);
}
