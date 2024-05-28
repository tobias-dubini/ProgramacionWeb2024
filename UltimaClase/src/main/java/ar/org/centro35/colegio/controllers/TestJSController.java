package ar.org.centro35.colegio.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestJSController {
    
    @GetMapping("/testJS")
    public String getTestJS() {
        return "testJS";
    }
    
    @GetMapping("/suscribe")
    public String getSubscribe() {
        return "suscribe";
    }
}
