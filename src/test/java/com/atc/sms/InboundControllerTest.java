package com.atc.sms;

import com.atc.sms.controller.InboundController;
import com.atc.sms.dto.SMSRequest;
import com.atc.sms.dto.SMSResponse;
import com.atc.sms.model.PhoneNumber;
import com.atc.sms.repository.PhoneNumberRepository;
import com.atc.sms.service.AuthenticationService;
import com.atc.sms.util.JedisUtility;
import com.atc.sms.util.SMSContants;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InboundControllerTest {

    @Mock
    AuthenticationService authenticationService;

    @Mock
    PhoneNumberRepository phoneNumberRepository;

    @Mock
    JedisUtility jedisUtility;

    InboundController inboundController;

    @Before
    public void setup() {
        inboundController = new InboundController(phoneNumberRepository, authenticationService, jedisUtility);
    }

    @Test
    public void testInboundServiceNoAuth() {

        HttpHeaders headers = null;
        SMSRequest smsRequest = new SMSRequest();

        ResponseEntity responseEntity = inboundController.inboundSMSService(headers, smsRequest);
        Assert.assertTrue(responseEntity.getStatusCode() == HttpStatus.FORBIDDEN);
    }

    @Test
    public void testInboundServiceMockAuth() {

        HttpHeaders headers = null;
        SMSRequest smsRequest = new SMSRequest();
        smsRequest.setFrom("123456789");
        smsRequest.setTo("98979898");
        smsRequest.setText("STOP");

        when(authenticationService.authenticate(any())).thenReturn(true);

        ResponseEntity responseEntity = inboundController.inboundSMSService(headers, smsRequest);
        Assert.assertTrue(responseEntity.getStatusCode() == HttpStatus.OK);
    }

    @Test
    public void testInboundServiceWithInValidParam() {

        HttpHeaders headers = null;
        SMSRequest smsRequest = new SMSRequest();
        smsRequest.setFrom("1");
        smsRequest.setTo("98979898");
        smsRequest.setText("STOP");

        when(authenticationService.authenticate(any())).thenReturn(true);
        ResponseEntity responseEntity = inboundController.inboundSMSService(headers, smsRequest);
        SMSResponse response = (SMSResponse) responseEntity.getBody();
        Assert.assertTrue(response.getError() == "'" + SMSContants.FROM + "' " + SMSContants.PARAM_INVALID);
    }

    @Test
    public void testInboundServiceWithValidPhoneNum() {

        HttpHeaders headers = null;
        SMSRequest smsRequest = new SMSRequest();
        smsRequest.setFrom("121111");
        smsRequest.setTo("98979898");
        smsRequest.setText("STOP");

        when(authenticationService.authenticate(any())).thenReturn(true);
        when(phoneNumberRepository.findByNumber(anyString())).thenReturn(new PhoneNumber());
        when(jedisUtility.getJedis()).thenReturn(new Jedis());

        ResponseEntity responseEntity = inboundController.inboundSMSService(headers, smsRequest);
        SMSResponse response = (SMSResponse) responseEntity.getBody();
        Assert.assertTrue(responseEntity.getStatusCode() == HttpStatus.OK);
        Assert.assertTrue(response.getError().equalsIgnoreCase(""));
        Assert.assertTrue(response.getMessage().equalsIgnoreCase(SMSContants.INBOUND_SMS_OK));
    }

}