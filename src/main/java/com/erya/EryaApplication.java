package com.erya;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.erya.dao")
@EnableScheduling
public class EryaApplication {

	public static void main(String[] args) {
		SpringApplication.run(EryaApplication.class, args);
	}

}
