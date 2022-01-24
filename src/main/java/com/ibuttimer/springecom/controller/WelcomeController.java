package com.ibuttimer.springecom.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.ibuttimer.springecom.config.Config.*;


@RestController
public class WelcomeController {

    @GetMapping("${spring.data.rest.base-path}" + HELLO_URL)
    public ResponseEntity<String> welcome() {
        return new ResponseEntity<>("{\"message\":\"welcome\"}", HttpStatus.OK);
    }
}
