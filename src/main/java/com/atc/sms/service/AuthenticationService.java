package com.atc.sms.service;

import com.atc.sms.model.Account;
import com.atc.sms.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    AccountRepository accountRepository;

    public boolean authenticate(HttpHeaders headers) {

        if (headers != null
                && headers.get("username") != null
                && headers.get("username").get(0) != null
                && headers.get("authId") != null
                && headers.get("authId").get(0) != null) {

            String userName = headers.get("username").get(0);
            String authId = headers.get("authId").get(0);

            Account account = accountRepository.authenticate(authId, userName);
            if (account != null) {
                return true;
            }
        }
        return false;
    }
}
