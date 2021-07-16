package br.com.webschool.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import br.com.webschool.domain.model.Teacher;

@Controller
public class LoginController {
/*     @GetMapping("/student")
    public String loginStudent(Model model){
        model.addAttribute(new Student());
        return "login-student";
    } */

    @GetMapping("/login/teacher")
    public String loginTeacher(Model model){
        model.addAttribute(new Teacher());
        return "login-teacher";
    }

    @PostMapping("/validateTeacher")
    public String saveStudent(@ModelAttribute Teacher teacher) {
        System.out.println(teacher.getEmail());
        System.out.println(teacher.getPassword());
        return "placeholder";
    }
}
