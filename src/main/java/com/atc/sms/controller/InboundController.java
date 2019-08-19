package com.atc.sms.controller;

import com.atc.sms.util.JedisUtility;
import com.atc.sms.util.SMSContants;
import com.atc.sms.dto.SMSRequest;
import com.atc.sms.dto.SMSResponse;
import com.atc.sms.model.PhoneNumber;
import com.atc.sms.repository.PhoneNumberRepository;
import com.atc.sms.service.AuthenticationService;
import com.atc.sms.util.SMSValidator;
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
    AuthenticationService authenticationService;

    public InboundController(PhoneNumberRepository phNumRepository,
                             AuthenticationService authService, JedisUtility jedisUtility) {

        this.phoneNumberRepository = phNumRepository;
        this.authenticationService = authService;
        this.jedisUtility = jedisUtility;
    }

    private String[] stopStrArr = {"STOP", "STOP\n", "STOP\r", "STOP\r\n"};

    @Value("${spring.redis.host}")
    private String host;

    @Value("${com.atc.cache.expiry.duration}")
    private int expiryDuration;

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
                jedis.expire(key, expiryDuration);
            }

            response.setMessage(SMSContants.INBOUND_SMS_OK);
            return new ResponseEntity(response, HttpStatus.OK);
        } catch (Exception e) {
            response.setError(SMSContants.UNEXPECTED_ERROR);
            return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
