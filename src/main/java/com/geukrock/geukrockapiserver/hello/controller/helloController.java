package com.geukrock.geukrockapiserver.hello.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class helloController {
    @GetMapping("/hello")
    public String hello(){
        return "Hello from api server!";
    }
}
