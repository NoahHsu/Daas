package org.daas.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
//http://localhost:8080/swagger-ui/index.html#/
@RestController
public class DemoController {
	@RequestMapping("/")
    String home() {
        return "Hello Noah!";
    }
}
