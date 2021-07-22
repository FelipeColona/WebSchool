package br.com.webschool.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/teachers")
@AllArgsConstructor
public class TeacherController {

    @Controller
    public class PageRenderer {
        @GetMapping("/teachers/login")
        public String loginPage(){
            return "teachers-login";
        }
    }

    @GetMapping()
    public String home(){
        return "Hello World!";
    }
}
