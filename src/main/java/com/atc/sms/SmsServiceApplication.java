package com.atc.sms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SmsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmsServiceApplication.class, args);
	}

}
