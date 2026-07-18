package com.careerconnect;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CareerConnectApplication {

    public static void main(String[] args) {
        SpringApplication.run(CareerConnectApplication.class, args);
        System.out.println("CareerConnect is up and running on port 8080");
    }
}
