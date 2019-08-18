package com.atc.sms.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.SequenceGenerator;
import javax.persistence.Column;

@Entity
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(generator = "account_generator")
    @SequenceGenerator(
            name = "account_generator",
            sequenceName = "account_id_seq",
            initialValue = 1000
    )
    private Long id;

    @Column(columnDefinition = "user_name")
    private String userName;

    @Column(columnDefinition = "auth_id")
    private String authId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthId() {
        return authId;
    }

    public void setAuthId(String authId) {
        this.authId = authId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
