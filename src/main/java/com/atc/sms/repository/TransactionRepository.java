package com.atc.sms.repository;

import com.atc.sms.model.Transaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface TransactionRepository extends CrudRepository<Transaction, Long> {

    @Query("SELECT t FROM transaction t where t.from LIKE :from AND t.to LIKE :to")
    Transaction findByFromAndTo(@Param("from") String from, @Param("to") String to);
}
