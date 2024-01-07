package com.example.projetgestiondereservations.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AppController {

    @GetMapping("/accueil")
    public String showPageAccuil() {
        return "app";
    }
}
