package com.claimrequest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping(value = { "/login"})
    public String login() {
        return "login";
    }

    @GetMapping("/logout")
    public String logout(){
        return "redirect:/home";
    }

}
