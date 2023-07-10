package com.ibuttimer.springecom.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HomeController {

    @Value("${app.sample-client}")
    String sampleClient;


    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("sample_client", sampleClient);
        return "index";
    }
}
