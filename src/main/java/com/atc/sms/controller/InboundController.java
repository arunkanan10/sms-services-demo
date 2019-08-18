package com.atc.sms.controller;

import com.atc.sms.JedisUtility;
import com.atc.sms.SMSContants;
import com.atc.sms.dto.SMSRequest;
import com.atc.sms.dto.SMSResponse;
import com.atc.sms.model.PhoneNumber;
import com.atc.sms.model.Transaction;
import com.atc.sms.repository.PhoneNumberRepository;
import com.atc.sms.repository.TransactionRepository;
import com.atc.sms.service.AuthenticationService;
import com.atc.sms.service.SMSValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestBody;
import redis.clients.jedis.Jedis;

import java.util.Arrays;
import java.util.List;

@RestController
public class InboundController {

    @Autowired
    PhoneNumberRepository phoneNumberRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    AuthenticationService authenticationService;

    private String[] stopStrArr = {"STOP", "STOP\n", "STOP\r", "STOP\r\n"};

    @Value("${spring.redis.host}")
    private String host;

    @Autowired
    JedisUtility jedisUtility;

    @PostMapping("/inbound/sms")
    public ResponseEntity inboundSMSService(@RequestHeader HttpHeaders headers, @RequestBody SMSRequest smsRequest) {

        SMSResponse response = new SMSResponse();

        if (!authenticationService.authenticate(headers)) {
             return new ResponseEntity(HttpStatus.FORBIDDEN);
        }

        String validation = SMSValidator.inputParamValidation(smsRequest);
        if (validation != null) {
            response.setError(validation);
            return new ResponseEntity(response, HttpStatus.OK);
        }

        PhoneNumber phoneNumber = phoneNumberRepository.findByNumber(smsRequest.getTo());
        if (phoneNumber == null) {
            response.setError(SMSContants.TO_PARAM_NOT_FOUND);
            return new ResponseEntity(response, HttpStatus.OK);
        }

        try {
            List<String> stopStrList = Arrays.asList(stopStrArr);
            if (stopStrList.contains(smsRequest.getText())) {

                Jedis jedis = jedisUtility.getJedis();
                String key = smsRequest.getFrom() + smsRequest.getTo();
                jedis.set(key, smsRequest.getText());
                jedis.expire(key, 4 * 3600); // Expiry time 4 hours
            }

            response.setMessage(SMSContants.ALL_PARAM_VALID);
            return new ResponseEntity(response, HttpStatus.OK);
        } catch (Exception e) {
            response.setError(SMSContants.UNEXPECTED_ERROR);
            return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
