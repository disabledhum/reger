package com.sch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class RegerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RegerApplication.class, args);
    }

}
