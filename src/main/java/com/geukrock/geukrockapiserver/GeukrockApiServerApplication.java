package com.geukrock.geukrockapiserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class GeukrockApiServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(GeukrockApiServerApplication.class, args);
    }
}
