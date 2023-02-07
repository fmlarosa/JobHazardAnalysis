package com.jha.accessingdatamysql.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

//This controller allows for redirection to the React app build in the jha-frontend directory
@Controller
public class ReactForwardController {
    @GetMapping(value = "/**/{path:[^\\.]*}")
    public String forward() { 
        return "forward:/";
    }
}
