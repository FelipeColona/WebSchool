package br.com.webschool.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/students")
public class StudentController {

    @Controller
    public class PageRenderer {
        @GetMapping("/students/login")
        public String loginPage(){
            return "students-login";
        }

        @GetMapping("/students")
        public String home(){
            return "students-home";
        }
    }
}
