package com.atc.sms.dto;

public class SMSResponse {
    private String message;
    private String error;

    public SMSResponse() {
        this.message = "";
        this.error = "";
    }

    public SMSResponse(String message, String error) {
        this.message = message;
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
