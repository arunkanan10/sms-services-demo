package com.atc.sms.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.SequenceGenerator;
import javax.persistence.Column;

@Entity
@Table(name = "phone_number")
public class PhoneNumber {

    @Id
    @GeneratedValue(generator = "phone_number_generator")
    @SequenceGenerator(
            name = "phone_number_generator",
            sequenceName = "account_id_seq",
            initialValue = 1000
    )
    private Long id;

    @Column(columnDefinition = "number")
    private  String number;

    @Column(columnDefinition = "account_id")
    private int accountId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }
}
