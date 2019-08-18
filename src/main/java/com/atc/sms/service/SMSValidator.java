package com.atc.sms.service;

import com.atc.sms.SMSContants;
import com.atc.sms.dto.SMSRequest;
import com.atc.sms.dto.SMSResponse;
import com.atc.sms.model.PhoneNumber;
import org.springframework.stereotype.Service;

@Service
public class SMSValidator {


    public static String inputParamValidation(SMSRequest smsRequest) {

        if (smsRequest == null) {
            return SMSContants.EMPTY_REQUEST;
        }

        if (smsRequest.getFrom() == null) {
            return "'" + SMSContants.FROM + "' " + SMSContants.PARAM_MISSING;
        }

        if (smsRequest.getTo() == null) {
            return "'" + SMSContants.TO + "' " + SMSContants.PARAM_MISSING;
        }

        if (smsRequest.getText() == null) {
            return "'" + SMSContants.TEXT + "' " + SMSContants.PARAM_MISSING;
        }

        if (!(smsRequest.getFrom().length() >= 6 && smsRequest.getFrom().length() <= 16)) {
            return "'" + SMSContants.FROM + "' " + SMSContants.PARAM_INVALID;
        }

        if (!(smsRequest.getTo().length() >= 6 && smsRequest.getTo().length() <= 16)) {
            return "'" + SMSContants.TO + "' " + SMSContants.PARAM_INVALID;
        }

        if (!(smsRequest.getText().length() >= 1 && smsRequest.getText().length() <= 120)) {
            return "'" + SMSContants.TO + "' " + SMSContants.PARAM_INVALID;
        }

        return null;
    }

    public static boolean validateFrom(PhoneNumber phoneNumber, SMSResponse response) {
        if (phoneNumber == null) {
            response.setError(SMSContants.FROM_NOT_FOUND);
            return true;
        }
        return false;
    }

}
