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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

@RestController
public class OutboundController {

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    PhoneNumberRepository phoneNumberRepository;

    @Autowired
    JedisUtility jedisUtility;

    @Value("${com.atc.cache.from.expiry.duration}")
    private int expiryDuration;

    @PostMapping("/outbound/sms")
    public ResponseEntity outboundSMSService(@RequestHeader HttpHeaders headers, @RequestBody SMSRequest smsRequest) {

        SMSResponse response = new SMSResponse();

        if (!authenticationService.authenticate(headers)) {
             return new ResponseEntity(HttpStatus.FORBIDDEN);
        }

        String validation = SMSValidator.inputParamValidation(smsRequest);
        if (validation != null) {
            response.setError(validation);
            return new ResponseEntity(response, HttpStatus.OK);
        }

        Jedis jedis = jedisUtility.getJedis();
        String key = smsRequest.getFrom() + smsRequest.getTo();
        String val = jedis.get(key);
        if (val != null) {
            String res = SMSContants.STOP_ERROR;
            response.setError(String.format(res, smsRequest.getFrom(), smsRequest.getTo()));
            return new ResponseEntity(response, HttpStatus.OK);
        }

        PhoneNumber phoneNumber = phoneNumberRepository.findByNumber(smsRequest.getFrom());
        if (SMSValidator.validateFrom(phoneNumber, response)) {
            return new ResponseEntity(response, HttpStatus.OK);
        }

        try {

            key = smsRequest.getFrom();
            val = jedis.get(key);
            if (val == null) {
                jedis.set(key, "1");
                jedis.expire(key, expiryDuration);
            } else {
                int n = Integer.parseInt(val);
                if (n <= 50) {
                    jedis.set(key, "" + ++n);
                } else {
                    String msg = SMSContants.LIMIT_EXCEED;
                    response.setError(String.format(msg, smsRequest.getFrom()));
                    return new ResponseEntity(response, HttpStatus.OK);
                }
            }

            response.setMessage(SMSContants.OUTBOUND_SMS_OK);
            return new ResponseEntity(response, HttpStatus.OK);
        } catch (Exception e) {
            response.setError(SMSContants.UNEXPECTED_ERROR);
            return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
